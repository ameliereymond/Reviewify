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
    min_correl: int = min(len(x_axis), len(y_axis))
    x_clean: List[float] = x_axis[:min_correl]
    y_clean: List[float] = y_axis[:min_correl]

    pyplot.scatter(x_clean, y_clean, alpha=.1, s=400)
    pyplot.title(plot_name)
    pyplot.show()
