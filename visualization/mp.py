from typing import List, Dict

import pandas
from matplotlib import pyplot


def boxplot_with_label(elements_names_to_list_of_values: Dict[str, List[float]], plotname: str):
    temp = {}
    for k in elements_names_to_list_of_values.keys():
        temp[k] = pandas.Series(elements_names_to_list_of_values[k])
    frame = pandas.DataFrame.from_dict(temp)
    frame.boxplot()
    pyplot.title(plotname)
    pyplot.show()
