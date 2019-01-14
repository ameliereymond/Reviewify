from typing import List, Iterable

import numpy as np

from sets.CustomerReview import CustomerReview


class Statistics:
    def __init__(self, mean: float, std: float, variance: float, median: float) -> None:
        self.mean: float = mean
        self.std: float = std
        self.variance: float = variance
        self.median: float = median

    def __str__(self) -> str:
        return str(self.__dict__)

    @staticmethod
    def from_review_set(review_set: List[CustomerReview], key_of_interest: callable) -> "Statistics":
        values: List = []
        for review in review_set:
            values.append(key_of_interest(review))
        return Statistics(
            float(np.mean(values)),
            float(np.std(values)),
            float(np.var(values)),
            float(np.median(values))
        )

    @staticmethod
    def merge(statistics: Iterable["Statistics"]) -> "Statistics":
        means: List[float] = list(map(lambda stat: stat.mean, statistics))
        stds: List[float] = list(map(lambda stat: stat.std, statistics))
        variances: List[float] = list(map(lambda stat: stat.variance, statistics))
        medians: List[float] = list(map(lambda stat: stat.median, statistics))
        return Statistics(
            float(np.mean(means)),
            float(np.mean(stds)),
            float(np.mean(variances)),
            float(np.mean(medians))
        )
