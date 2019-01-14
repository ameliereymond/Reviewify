from typing import List, Dict

import pandas
from matplotlib import pyplot


def layered_histogram(elements_names_to_list_of_values: Dict[str, List[float]], plotname: str):
    temp = {}
    for k in elements_names_to_list_of_values.keys():
        temp[k] = pandas.Series(elements_names_to_list_of_values[k])
    frame = pandas.DataFrame.from_dict(temp)
    frame.boxplot()
    pyplot.title(plotname)
    pyplot.show()


def layered_histogram_int(elements_names_to_list_of_values: Dict[int, List[float]], plotname: str):
    temp = {}
    for k in elements_names_to_list_of_values.keys():
        temp[k] = pandas.Series(elements_names_to_list_of_values[k])
    frame = pandas.DataFrame.from_dict(temp)
    frame.boxplot()
    pyplot.title(plotname)
    pyplot.show()
