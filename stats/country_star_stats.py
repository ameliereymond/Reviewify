from typing import List

import numpy as np

from sets.CustomerReview import CustomerReview


class CountryStarStats:
    def __init__(self, mean: float, std: float, variance: float, median: float) -> None:
        self.mean: float = mean
        self.std: float = std
        self.variance: float = variance
        self.median: float = median

    def __str__(self) -> str:
        return str(self.__dict__)


def from_review_set(country_review_set: List[CustomerReview]) -> CountryStarStats:
    stars: List[int] = []
    for review in country_review_set:
        stars.append(review.star_rating)
    return CountryStarStats(
        float(np.mean(stars)),
        float(np.std(stars)),
        float(np.var(stars)),
        float(np.median(stars))
    )
