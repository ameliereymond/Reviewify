import os
from typing import Set, List, Dict

from safetychecks import safety_check
from sets import findsets, loadsets
from sets.CustomerReview import CustomerReview
from sets.reviews import ReviewSet
from stats.stat import Statistics
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
star_stats_per_country: Dict[str, Statistics] = {}
print("Star stats per country:")
for country in cleaned_sets.keys():
    cleaned_set_country: List[CustomerReview] = cleaned_sets[country]
    star_stats_per_country[country] = Statistics.from_review_set(cleaned_set_country, lambda a_review: a_review.star_rating)
    print("- " + country + " -> " + str(star_stats_per_country[country]))

# Get stats of sentiment per country
sentiment_stats_per_country: Dict[str, Statistics] = {}
print("Sentiment stats per country:")
for country in cleaned_sets.keys():
    cleaned_set_country: List[CustomerReview] = cleaned_sets[country]
    sentiment_stats_per_country[country] = Statistics.from_review_set(cleaned_set_country, lambda a_review: a_review.sentiment_analysis_score)
    print("- " + country + " -> " + str(sentiment_stats_per_country[country]))

# Get stats of helpfulness per country
helpfulness_stats_per_country: Dict[str, Statistics] = {}
print("Sentiment stats per country:")
for country in cleaned_sets.keys():
    cleaned_set_country: List[CustomerReview] = cleaned_sets[country]
    helpfulness_stats_per_country[country] = Statistics.from_review_set(cleaned_set_country, lambda a_review: a_review.helpful_votes)
    print("- " + country + " -> " + str(helpfulness_stats_per_country[country]))

# Boxplot star values per country
star_values_per_country: Dict[str, List[int]] = {}
for country in cleaned_sets.keys():
    star_values_per_country[country] = list(map(
        lambda a_review: a_review.star_rating,
        cleaned_sets[country]
    ))
mp.boxplot(star_values_per_country, "Stars by country")

# Boxplot sentiment values per country
sentiment_values_per_country: Dict[str, List[int]] = {}
for country in cleaned_sets.keys():
    sentiment_values_per_country[country] = list(map(
        lambda a_review: a_review.sentiment_analysis_score,
        cleaned_sets[country]
    ))
mp.boxplot(sentiment_values_per_country, "Sentiment values per country")

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
mp.boxplot(sentiment_scores_per_stars, "Sentiment scores by stars")

# Plot sentiment by helpfulness across all countries using stats to eliminate outliers
sentiment_stats_across_all_countries: Statistics = Statistics.merge(sentiment_stats_per_country.values())
helpfulness_stats_across_all_countries: Statistics = Statistics.merge(helpfulness_stats_per_country.values())

min_helpful_votes: int = helpfulness_stats_across_all_countries.median - helpfulness_stats_across_all_countries.std
max_helpful_votes: int = helpfulness_stats_across_all_countries.median + helpfulness_stats_across_all_countries.std
min_sent_score: int = sentiment_stats_across_all_countries.median - sentiment_stats_across_all_countries.std
max_sent_score: int = sentiment_stats_across_all_countries.median + sentiment_stats_across_all_countries.std

sentiment_scores: List[float] = []
helpfulnesses: List[int] = []
for country in cleaned_sets.keys():
    for review in cleaned_sets[country]:
        if min_helpful_votes < review.helpful_votes < max_helpful_votes:
            helpfulnesses.append(review.helpful_votes)
        if min_sent_score < review.sentiment_analysis_score < max_sent_score:
            sentiment_scores.append(review.sentiment_analysis_score)
mp.scatter_plot(helpfulnesses, sentiment_scores, "Sentiment scores by helpful votes (ignoring outliers)")
