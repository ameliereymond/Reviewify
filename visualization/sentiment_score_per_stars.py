from typing import Dict, List

from sets.CustomerReview import CustomerReview
from visualization import mp


def plot(reviews_by_stars: Dict[int, List[CustomerReview]]) -> None:
    # Boxplot sentiment score per stars
    sentiment_scores_per_stars: Dict[str, List[float]] = {}
    for star_count in reviews_by_stars.keys():
        sentiment_scores_per_stars[str(star_count)] = list(map(
            lambda a_review: a_review.sentiment_analysis_score,
            reviews_by_stars[star_count]
        ))
    mp.boxplot(sentiment_scores_per_stars, "Sentiment scores by star rating", "Star rating", "Sentiment score")
