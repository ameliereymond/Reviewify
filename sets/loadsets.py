from typing import List

import utils
from sets import CustomerReview
from sets.reviews import ReviewSet


def load_set(set_path: str, lines_to_take: int) -> ReviewSet:
    reviews: List[CustomerReview] = []
    lines_taken: int = 0
    with open(set_path, mode="r", encoding="utf8") as dataset:
        for line in dataset.readlines():
            if not lines_taken == lines_to_take:
                # ignore header line
                if not line.startswith("marketplace"):
                    review_components: List[str] = line.split("\t")
                    review: CustomerReview = utils.pass_array_as_arguments(CustomerReview.CustomerReview, review_components)
                    reviews.append(review)
                    lines_taken = lines_taken + 1
        dataset.close()
    return ReviewSet(reviews)


def set_size(set_path: str) -> int:
    lines: int = 0
    with open(set_path, mode="r", encoding="utf8") as dataset:
        for _ in dataset.readlines():
            lines = lines + 1
        dataset.close()
    return lines
