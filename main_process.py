from python_java_io import pj_io
from dm_hclust import partition, dendrogram_

input_output = pj_io()
x = input_output.read_matrix()
instances_name = input_output.read_str_list()
method = input_output.read_str()
err, results = partition(x, method)
input_output.write_int(err)
if err == 1:
    no_clusters, cluster_labels, h = results
    input_output.write_int(int(no_clusters))
    input_output.write_str_list(cluster_labels)
    input_output.write_matrix(h)
    dendrogram_(h, instances_name, "Optimal partition", no_clusters)
else:
    input_output.write_str(results)
