from typing import List

from sets.CustomerReview import CustomerReview


def safety_check(reviews: List[CustomerReview]) -> List[CustomerReview]:
    to_remove: List[CustomerReview] = []

    # Correct timestamps
    for review_i in range(len(reviews) - 1):
        a_review = reviews[review_i]
        no_helpful_vote = a_review.helpful_votes < 1
        invalid_body = a_review.review_body is None or len(a_review.review_body) is 0 or len(a_review.review_body) > 5000
        if no_helpful_vote or invalid_body:
            to_remove.append(a_review)

    print("\t-> Got " + str(len(to_remove)) + " review to remove as they did not fit requirements.")
    for review_to_remove in to_remove:
        reviews.remove(review_to_remove)

    return reviews
