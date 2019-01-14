from typing import Dict, List

from sets.CustomerReview import CustomerReview
from visualization import mp


def plot(cleaned_sets: Dict[str, List[CustomerReview]]) -> None:
    """
    Boxplot star values per country
    """
    star_values_per_country: Dict[str, List[int]] = {}
    for country in cleaned_sets.keys():
        star_values_per_country[country] = list(map(
            lambda a_review: a_review.star_rating,
            cleaned_sets[country]
        ))
    mp.boxplot(star_values_per_country, "Stars by country")
