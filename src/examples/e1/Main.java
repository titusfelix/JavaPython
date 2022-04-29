package examples.e1;

import main.DataFrame;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        try {
            DataFrame dataFrame = new DataFrame("ADN_Tari.csv", ",", true, 0);
            try(HClust hClust = new HClust(dataFrame.getData(), dataFrame.getIndex())) {
                hClust.start("main_process.py");
                hClust.fit();
                System.out.println("Number of clusters:"+hClust.getNoClusters());
                System.out.println("Linkage matrix:");
                double[][] h = hClust.getH();
                for (double[] join:h){
                    System.out.println(Arrays.toString(join));
                }
                System.out.println("Partition:");
                System.out.println(Arrays.toString(hClust.getCluster_labels()));
            }
        }
        catch (Exception ex){
            System.err.println(ex);
        }
    }
}
