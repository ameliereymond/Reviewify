import os
from os import listdir
from os.path import join
from typing import List, Set

DATASET_FOLDER = "data/samples"
if "reviewify_full" in os.environ:
    DATASET_FOLDER = "data"


def find_sets_paths() -> Set[str]:
    files_in_data_directory: List[str] = listdir(DATASET_FOLDER)
    tsv_sets = filter(lambda fname: fname.endswith(".tsv"), files_in_data_directory)
    base_path_of_sets = join(os.path.abspath("."), DATASET_FOLDER)
    return set(map(lambda set_name: join(base_path_of_sets, set_name), tsv_sets))
