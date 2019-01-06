from typing import List

from sets import CustomerReview


class ReviewSet:
    def __init__(self, reviews: list) -> None:
        self.reviews: List[CustomerReview] = reviews

    def marketplace(self) -> str:
        review: CustomerReview = self.reviews[0]
        return review.marketplace

    def __str__(self) -> str:
        return self.marketplace() + " -> " + str(len(self.reviews)) + " reviews"
