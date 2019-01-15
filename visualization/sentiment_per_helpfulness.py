from typing import Dict, List, Iterable

from sets.CustomerReview import CustomerReview
from stats.stat import Statistics
from visualization import mp


def plot_country(
        country_reviews: List[CustomerReview],
        helpfulness_stats_country: Statistics,
        country: str,
        deviations: int
) -> None:
    min_helpful_votes: int = helpfulness_stats_country.median - helpfulness_stats_country.std * deviations
    max_helpful_votes: int = helpfulness_stats_country.median + helpfulness_stats_country.std * deviations

    sentiment_scores: List[float] = []
    helpfulnesses: List[int] = []
    for review in country_reviews:
        if min_helpful_votes < review.helpful_votes < max_helpful_votes:
            helpfulnesses.append(review.helpful_votes)
            sentiment_scores.append(review.sentiment_analysis_score)
    mp.scatter_plot(
        helpfulnesses,
        sentiment_scores,
        "Sentiment scores by helpful votes for " + country + " (within median ± " + str(deviations) + " σ)",
        "Helpful votes",
        "Sentiment score"
    )


def plot_global(
        cleaned_sets: Dict[str, List[CustomerReview]],
        helpfulness_stats: Iterable[Statistics],
        sentiment_stats: Iterable[Statistics],
        deviations: int
) -> None:
    # Plot sentiment by helpfulness across all countries using stats to eliminate outliers
    sentiment_stats_across_all_countries: Statistics = Statistics.merge(sentiment_stats)
    helpfulness_stats_across_all_countries: Statistics = Statistics.merge(helpfulness_stats)

    min_helpful_votes: int = helpfulness_stats_across_all_countries.median - helpfulness_stats_across_all_countries.std * deviations
    max_helpful_votes: int = helpfulness_stats_across_all_countries.median + helpfulness_stats_across_all_countries.std * deviations
    min_sent_score: int = sentiment_stats_across_all_countries.median - sentiment_stats_across_all_countries.std * deviations
    max_sent_score: int = sentiment_stats_across_all_countries.median + sentiment_stats_across_all_countries.std * deviations

    sentiment_scores: List[float] = []
    helpfulnesses: List[int] = []
    for country in cleaned_sets.keys():
        for review in cleaned_sets[country]:
            if min_helpful_votes < review.helpful_votes < max_helpful_votes:
                helpfulnesses.append(review.helpful_votes)
            if min_sent_score < review.sentiment_analysis_score < max_sent_score:
                sentiment_scores.append(review.sentiment_analysis_score)
    mp.scatter_plot(
        helpfulnesses,
        sentiment_scores,
        "Sentiment scores by helpful votes (within median ± " + str(deviations) + " σ)",
        "Helpful votes",
        "Sentiment score"
    )
