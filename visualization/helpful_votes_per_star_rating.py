from typing import Dict, List, Iterable

from sets.CustomerReview import CustomerReview
from stats.stat import Statistics
from visualization import mp


# Boxplot helpfulness by star rating


def plot_country(country_set: List[CustomerReview], country_helpful_votes_stats: Statistics, country_name: str):
    min_helpful_votes: int = country_helpful_votes_stats.median - country_helpful_votes_stats.std
    max_helpful_votes: int = country_helpful_votes_stats.median + country_helpful_votes_stats.std

    helpfulnesses_per_star_rating: Dict[int, List[int]] = {}
    for review in country_set:
        if min_helpful_votes < review.helpful_votes < max_helpful_votes:
            if review.star_rating not in helpfulnesses_per_star_rating.keys():
                helpfulnesses_per_star_rating[review.star_rating] = []
            helpfulnesses_per_star_rating[review.star_rating].append(review.helpful_votes)
    mp.boxplot(
        helpfulnesses_per_star_rating,
        "Helpful votes count per star rating for " + country_name,
        "Star rating",
        "Helpful votes"
    )


def plot_global(
        cleaned_sets: Dict[str, List[CustomerReview]],
        helpfulness_stats: Iterable[Statistics]
) -> None:
    helpfulness_stats_across_all_countries: Statistics = Statistics.merge(helpfulness_stats)

    min_helpful_votes: int = helpfulness_stats_across_all_countries.median - helpfulness_stats_across_all_countries.std
    max_helpful_votes: int = helpfulness_stats_across_all_countries.median + helpfulness_stats_across_all_countries.std

    helpful_votes_per_star_rating: Dict[int, List[int]] = {}
    for country in cleaned_sets.keys():
        for review in cleaned_sets[country]:
            if min_helpful_votes < review.helpful_votes < max_helpful_votes:
                if review.star_rating not in helpful_votes_per_star_rating:
                    helpful_votes_per_star_rating[review.star_rating] = []
                helpful_votes_per_star_rating[review.star_rating].append(review.helpful_votes)

    mp.boxplot(
        helpful_votes_per_star_rating,
        "Helpful votes count per star rating (within median ± 1 σ)",
        "Star rating",
        "Helpful votes"
    )
