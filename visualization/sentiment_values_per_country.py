from typing import Dict, List

from sets.CustomerReview import CustomerReview
from visualization import mp


def plot(cleaned_sets: Dict[str, List[CustomerReview]]) -> None:
    """
    Boxplot sentiment values per country
    """
    sentiment_values_per_country: Dict[str, List[int]] = {}
    for country in cleaned_sets.keys():
        sentiment_values_per_country[country] = list(map(
            lambda a_review: a_review.sentiment_analysis_score,
            cleaned_sets[country]
        ))
    mp.boxplot(sentiment_values_per_country, "Sentiment values per country")
