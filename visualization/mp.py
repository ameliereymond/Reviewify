from typing import List, Dict

import pandas
from matplotlib import pyplot


def boxplot(elements_names_to_list_of_values: Dict[str, List[float]], plot_name: str):
    temp = {}
    for k in elements_names_to_list_of_values.keys():
        temp[k] = pandas.Series(elements_names_to_list_of_values[k])
    frame = pandas.DataFrame.from_dict(temp)
    frame.boxplot()
    pyplot.title(plot_name)
    pyplot.show()


def scatter_plot(x_axis: List[float], y_axis: List[float], plot_name: str):
    pyplot.scatter(x_axis, y_axis, alpha=.1, s=400)
    pyplot.title(plot_name)
    pyplot.show()
