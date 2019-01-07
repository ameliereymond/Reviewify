from typing import List

import numpy as np

from sets.CustomerReview import CustomerReview
from stats.country_star_stats import CountryStarStats


def ratinglist(reviews: List[CustomerReview]) -> CountryStarStats:
    ratings_list: List[int] = list(map(lambda review: review.star_rating, reviews))
    mean: float = np.mean(ratings_list)
    std = np.std(ratings_list)
    variance = np.var(ratings_list)
    median = np.median(ratings_list)

    return CountryStarStats(mean, std, variance, median)
