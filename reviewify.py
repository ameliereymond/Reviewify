import os
from typing import Set, List, Dict

from safetychecks import safety_check
from sets import findsets, loadsets
from sets.CustomerReview import CustomerReview
from sets.reviews import ReviewSet

print("Starting Reviewify with working directory : " + str(os.path.realpath(".")))

review_sets_paths: Set[str] = findsets.find_sets_paths()
print("Found the following review sets : " + str(review_sets_paths))

review_sets_loaded: Set[ReviewSet] = set()
for review_set_path in review_sets_paths:
    review_set_loaded = loadsets.load_set(review_set_path)
    review_sets_loaded.add(review_set_loaded)
print("Loaded the following review sets : " + str(list(map(str, review_sets_loaded))))

cleaned_sets: Dict[str, List[CustomerReview]] = {}

for review_set in review_sets_loaded:
    print("Set : " + review_set.marketplace())
    marketplace_name: str = review_set.marketplace()
    marketplace_reviews: List[CustomerReview] = review_set.reviews
    cleaned_sets[marketplace_name] = safety_check(marketplace_reviews)

