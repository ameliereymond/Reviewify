from typing import List, Set

from sets.CustomerReview import CustomerReview


def safety_check(reviews: List[CustomerReview]) -> List[CustomerReview]:
    seen_reviews: Set[str] = set()

    # Correct timestamps
    for a_review in reviews:
        if a_review.helpful_votes < 1:
            reviews.remove(a_review)
        if a_review.review_body is None or len(a_review.review_body) is 0:
            reviews.remove(a_review)
        if a_review.review_id in seen_reviews:
            reviews.remove(a_review)
        else:
            seen_reviews.add(a_review.review_id)

    return reviews
