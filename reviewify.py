import os
from typing import Set, List, Dict

import sent_analysis
import star_ratings
from safetychecks import safety_check
from sets import findsets, loadsets
from sets.CustomerReview import CustomerReview
from sets.reviews import ReviewSet
from stats import country_star_stats, country_sentiment
from stats.country_sentiment import CountrySentimentStats
from stats.country_star_stats import CountryStarStats
from visualization import mp

print("Starting Reviewify with working directory : " + str(os.path.realpath(".")))

review_sets_paths: Set[str] = findsets.find_sets_paths()
print("Found the following review sets : " + str(review_sets_paths))

review_sets_loaded: Set[ReviewSet] = set()
for review_set_path in review_sets_paths:
    review_set_loaded = loadsets.load_set(review_set_path)
    review_sets_loaded.add(review_set_loaded)
print("Loaded the following review sets : " + str(list(map(str, review_sets_loaded))))

cleaned_sets: Dict[str, List[CustomerReview]] = {}

for review_set in review_sets_loaded:
    print("Set : " + review_set.marketplace())
    marketplace_name: str = review_set.marketplace()
    marketplace_reviews: List[CustomerReview] = review_set.reviews
    cleaned_sets[marketplace_name] = safety_check(marketplace_reviews)

compounds_per_country: Dict[str, List[float]] = {}

for country in cleaned_sets.keys():
    compounds_per_country[country] = []
    cleaned_set_country: List[CustomerReview] = cleaned_sets[country]
    print("Sentiment analysis : " + country)
    for review in cleaned_set_country:
        score_rev: float = sent_analysis.sentiment_analyzer_scores(review.review_body)
        compounds_per_country[country].append(score_rev)

print(str(compounds_per_country))

star_stats_per_country: Dict[str, CountryStarStats] = {}
for country in cleaned_sets.keys():
    cleaned_set_country: List[CustomerReview] = cleaned_sets[country]
    print("Star ratings : " + country)
    star_stats_per_country[country] = star_ratings.ratinglist(cleaned_set_country)

print(star_stats_per_country["FR"].mean)
print(star_stats_per_country["DE"].mean)
print(star_stats_per_country["US"].mean)
print(star_stats_per_country["UK"].mean)

fr_star_stats: CountryStarStats = country_star_stats.from_review_set(cleaned_sets["FR"])
print(fr_star_stats)
de_star_stats: CountryStarStats = country_star_stats.from_review_set(cleaned_sets["DE"])
us_star_stats: CountryStarStats = country_star_stats.from_review_set(cleaned_sets["US"])
uk_star_stats: CountryStarStats = country_star_stats.from_review_set(cleaned_sets["UK"])

sentiment_stats_per_country: Dict[str, CountrySentimentStats] = {}
for country in cleaned_sets.keys():
    cleaned_set_country: List[CustomerReview] = cleaned_sets[country]
    sentiment_stats_per_country[country] = country_sentiment.from_review_set(cleaned_set_country)

star_values_per_country: Dict[str, List[int]] = {}
for country in cleaned_sets.keys():
    star_values_per_country[country] = list(map(lambda a_review: a_review.star_rating, cleaned_sets[country]))

sentiment_values_per_country: Dict[str, List[int]] = {}
for country in cleaned_sets.keys():
    sentiment_values_per_country[country] = list(
        map(lambda a_review: a_review.sentiment_analysis_score, cleaned_sets[country]))
mp.layered_histogram(star_values_per_country, "Stars by country")
mp.layered_histogram(sentiment_values_per_country, "Sentiment values per country")

# Box plots for each star rating
reviews_by_stars: Dict[int, List[CustomerReview]] = {}
for country in cleaned_sets.keys():
    for review in cleaned_sets[country]:
        if review.star_rating not in reviews_by_stars.keys():
            reviews_by_stars[review.star_rating] = []
        reviews_by_stars[review.star_rating].append(review)

sentiment_scores_per_stars: Dict[int, List[float]] = {}
for star_count in reviews_by_stars.keys():
    sentiment_scores_per_stars[star_count] = list(map(
        lambda a_review: a_review.sentiment_analysis_score,
        reviews_by_stars[star_count]
    ))


mp.layered_histogram_int(sentiment_scores_per_stars, "Sentiment scores by stars")
