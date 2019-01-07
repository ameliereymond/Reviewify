"""
Follows the type on Amazon's side :
marketplace
customer_id
review_id
product_id
product_parent
product_title
product_category
star_rating
helpful_votes
total_votes	vine
verified_purchase
review_headline
review_body
review_date
"""
from datetime import datetime


class CustomerReview:

    def __init__(
            self,
            marketplace,
            customer_id,
            review_id,
            product_id,
            product_parent,
            product_title,
            product_category,
            star_rating,
            helpful_votes,
            total_votes,
            vine,
            verified_purchase,
            review_headline,
            review_body,
            review_date
    ) -> None:
        self.marketplace: str = marketplace
        self.customer_id: int = customer_id
        self.review_id: str = review_id
        self.product_id: str = product_id
        self.product_parent: str = product_parent
        self.product_title: str = product_title
        self.product_category: str = product_category
        self.star_rating: int = star_rating
        self.helpful_votes: int = helpful_votes
        self.total_votes: int = total_votes
        self.vine: bool = vine
        self.verified_purchase: bool = verified_purchase
        self.review_headline: str = review_headline
        self.review_body: str = review_body
        self.review_date: datetime = review_date
