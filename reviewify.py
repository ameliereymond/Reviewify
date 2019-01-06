import os
from typing import Set

from sets import sets

print("Starting Reviewify with working directory : " + str(os.path.realpath(".")))

review_sets: Set[str] = sets.get_sets_paths()
print("Found the following review sets : " + str(review_sets))
