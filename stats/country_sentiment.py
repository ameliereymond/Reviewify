from typing import List

import numpy as np

from sets.CustomerReview import CustomerReview


class CountrySentimentStats:
    def __init__(self, mean: float, std: float, variance: float, median: float) -> None:
        self.mean: float = mean
        self.std: float = std
        self.variance: float = variance
        self.median: float = median

    def __str__(self) -> str:
        return str(self.__dict__)


def from_review_set(country_review_set: List[CustomerReview]) -> CountrySentimentStats:
    sentiment_values: List[int] = []
    for review in country_review_set:
        sentiment_values.append(review.sentiment_value)
    return CountrySentimentStats(
        float(np.mean(sentiment_values)),
        float(np.std(sentiment_values)),
        float(np.var(sentiment_values)),
        float(np.median(sentiment_values))
    )
