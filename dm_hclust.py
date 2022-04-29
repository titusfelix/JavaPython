import numpy as np
import pandas as pd
import scipy.cluster.hierarchy as hclust
import matplotlib.pyplot as plt
from scipy.cluster.hierarchy import dendrogram


def partition(x, method):
    try:
        h = hclust.linkage(x, method=method)
        p = h.shape[0]
        k_dif_max = np.argmax(h[1:, 2] - h[:(p - 1), 2])
        nr_clusteri = p - k_dif_max
        n = p + 1
        c = np.arange(n)
        for i in range(n - nr_clusteri):
            k1 = h[i, 0]
            k2 = h[i, 1]
            c[c == k1] = n + i
            c[c == k2] = n + i
        coduri = pd.Categorical(c).codes
        return 1, (nr_clusteri, np.array(["c" + str(cod + 1) for cod in coduri]), h)
    except Exception as ex:
        return 0, str(ex)


def dendrogram_(h, instante, titlu, nr_clusteri):
    p = h.shape[0]
    k_dif_max = p - nr_clusteri
    threshold = (h[k_dif_max, 2] + h[k_dif_max + 1, 2]) / 2
    fig = plt.figure(figsize=(15, 9))
    ax = fig.add_subplot(1, 1, 1)
    ax.set_title(titlu, fontsize=18, color='c')
    dendrogram(h, labels=instante, ax=ax, color_threshold=threshold)
    plt.show()
