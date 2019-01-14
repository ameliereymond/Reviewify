import os
from typing import Set, List, Dict

import star_ratings
from safetychecks import safety_check
from sets import findsets, loadsets
from sets.CustomerReview import CustomerReview
from sets.reviews import ReviewSet
from stats import country_sentiment
from stats.country_sentiment import CountrySentimentStats
from stats.country_star_stats import CountryStarStats
from visualization import mp

print("Starting Reviewify with working directory : " + str(os.path.realpath(".")))

# Look into folder to find out what sets are available
review_sets_paths: Set[str] = findsets.find_sets_paths()
print("Found the following review sets : " + str(review_sets_paths))

# Find out shortest set's length
min_set_len = min(list(map(lambda rs: loadsets.set_size(rs), review_sets_paths)))
print("Shortest set has " + str(min_set_len) + " lines. Only getting that much from each.")

# Load sets stopping at the number of reviews in the shortest set to get equal set lengths
review_sets_loaded: Set[ReviewSet] = set()
for review_set_path in review_sets_paths:
    review_set_loaded = loadsets.load_set(review_set_path, min_set_len)
    review_sets_loaded.add(review_set_loaded)
print("Loaded the following review sets : " + str(list(map(str, review_sets_loaded))))

# Keep only basic infos about a set's reviews after cleanup
cleaned_sets: Dict[str, List[CustomerReview]] = {}
for review_set in review_sets_loaded:
    print("Set : " + review_set.marketplace())
    marketplace_name: str = review_set.marketplace()
    print("\t-> Marketplace : " + marketplace_name)
    marketplace_reviews: List[CustomerReview] = review_set.reviews
    print("\t-> Review taken : " + str(len(marketplace_reviews)))
    cleaned_sets[marketplace_name] = safety_check(marketplace_reviews)
    print("\t-> Finished importing set " + marketplace_name)

# Get stats of stars per country
star_stats_per_country: Dict[str, CountryStarStats] = {}
for country in cleaned_sets.keys():
    cleaned_set_country: List[CustomerReview] = cleaned_sets[country]
    print("Star ratings : " + country)
    star_stats_per_country[country] = star_ratings.ratinglist(cleaned_set_country)

# Get stats of sentiment per country
sentiment_stats_per_country: Dict[str, CountrySentimentStats] = {}
for country in cleaned_sets.keys():
    cleaned_set_country: List[CustomerReview] = cleaned_sets[country]
    sentiment_stats_per_country[country] = country_sentiment.from_review_set(cleaned_set_country)

# Boxplot star values per country
star_values_per_country: Dict[str, List[int]] = {}
for country in cleaned_sets.keys():
    star_values_per_country[country] = list(map(lambda a_review: a_review.star_rating, cleaned_sets[country]))
mp.boxplot_with_label(star_values_per_country, "Stars by country")

# Boxplot sentiment values per country
sentiment_values_per_country: Dict[str, List[int]] = {}
for country in cleaned_sets.keys():
    sentiment_values_per_country[country] = list(
        map(lambda a_review: a_review.sentiment_analysis_score, cleaned_sets[country]))
mp.boxplot_with_label(sentiment_values_per_country, "Sentiment values per country")

# Get reviews classified by star values
reviews_by_stars: Dict[int, List[CustomerReview]] = {}
for country in cleaned_sets.keys():
    for review in cleaned_sets[country]:
        if review.star_rating not in reviews_by_stars.keys():
            reviews_by_stars[review.star_rating] = []
        reviews_by_stars[review.star_rating].append(review)

# Boxplot sentiment score per stars
sentiment_scores_per_stars: Dict[str, List[float]] = {}
for star_count in reviews_by_stars.keys():
    sentiment_scores_per_stars[str(star_count)] = list(map(
        lambda a_review: a_review.sentiment_analysis_score,
        reviews_by_stars[star_count]
    ))
mp.boxplot_with_label(sentiment_scores_per_stars, "Sentiment scores by stars")

