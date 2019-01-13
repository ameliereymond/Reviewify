from typing import List, Dict

import pandas


def layered_histogram(elements_names_to_list_of_values: Dict[str, List[float]], label_x_axis: str, label_y_axis: str):
    temp = {}
    for k in elements_names_to_list_of_values.keys():
        temp[k] = pandas.Series(elements_names_to_list_of_values[k])
    frame = pandas.DataFrame.from_dict(temp)
    frame.boxplot()
