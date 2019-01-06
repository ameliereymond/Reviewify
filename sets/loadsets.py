from typing import List

import utils
from sets import CustomerReview
from sets.reviews import ReviewSet


def load_set(set_path: str) -> ReviewSet:
    reviews: List[CustomerReview] = []
    with open(set_path, mode="r", encoding="utf8") as dataset:
        for line in dataset.readlines():
            # ignore header line
            if not line.startswith("marketplace"):
                review_components: List[str] = line.split("\t")
                review: CustomerReview = utils.pass_array_as_arguments(CustomerReview.CustomerReview, review_components)
                reviews.append(review)
        dataset.close()
    return ReviewSet(reviews)
