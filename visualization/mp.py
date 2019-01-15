from collections import OrderedDict
from typing import List, Dict, Any

import pandas
from matplotlib import pyplot


def boxplot(elements_names_to_list_of_values: Dict[Any, List[float]], plot_name: str, xlabel: str, ylabel: str):
    elements_names_to_list_of_values = OrderedDict(sorted(elements_names_to_list_of_values.items(), key=lambda t: t[0]))
    temp = {}
    for k in elements_names_to_list_of_values.keys():
        temp[k] = pandas.Series(elements_names_to_list_of_values[k])
    frame = pandas.DataFrame.from_dict(temp)
    frame.boxplot()
    pyplot.title(plot_name)
    pyplot.xlabel(xlabel)
    pyplot.ylabel(ylabel)
    pyplot.savefig("plot_out/" + plot_name + ".png")
    pyplot.figure()


def scatter_plot(x_axis: List[float], y_axis: List[float], plot_name: str, xlabel: str, ylabel: str):
    x_axis = list(sorted(x_axis))
    size_ideal: int = min(40, int(10_000_000 / (len(x_axis) * len(y_axis))) + 10)
    min_correl: int = min(len(x_axis), len(y_axis))
    x_clean: List[float] = x_axis[:min_correl]
    y_clean: List[float] = y_axis[:min_correl]

    pyplot.scatter(x_clean, y_clean, alpha=.1, s=size_ideal)
    pyplot.title(plot_name)
    pyplot.xlabel(xlabel)
    pyplot.ylabel(ylabel)
    pyplot.savefig("plot_out/" + plot_name + ".png")
    pyplot.figure()
