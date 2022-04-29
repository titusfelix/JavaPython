package examples.e1;

import javapython.PythonProcess;

public class HClust extends PythonProcess {
    private double[][] x,h;
    private String[] labels,cluster_labels;
    private int noClusters;

    public HClust(double[][] x, String[] labels) {
        this.x = x;this.labels = labels;
    }

    public void fit() throws Exception {
        jpio.write(x);jpio.write(labels);
        jpio.write("ward");
        jpio.getOut().flush();
        if (jpio.readInt() == 0) {
            System.out.println(jpio.readString());
        } else {
            noClusters = jpio.readInt();
            cluster_labels = jpio.readStringArray();
            h = jpio.readMatrix();
        }
    }

    public double[][] getH() {
        return h;
    }

    public String[] getCluster_labels() {
        return cluster_labels;
    }

    public int getNoClusters() {
        return noClusters;
    }
}
