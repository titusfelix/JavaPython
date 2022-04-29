package javapython;

import java.io.*;
import java.util.Arrays;

public class PythonProcess implements Closeable {
    private ProcessBuilder pb;
    protected Process p;
    protected JavaPythonIO jpio;

    public void start(String pythonScript) throws Exception {
        pb = new ProcessBuilder("python", pythonScript);
        p = pb.start();
        jpio = new JavaPythonIO(p.getOutputStream(), p.getInputStream());
    }

    public void verificare() throws Exception {
        try (OutputStream out = jpio.getOut()) {
            jpio.write(1);

            jpio.write(-98);
            jpio.write(-3.89765);
            jpio.write(new int[]{-100, 300, 890, 9999});
            jpio.write("Sirul trimis.");
            jpio.write(new double[]{9.08, 3.14, -99008.89});
            jpio.write(new String[]{"Ala", "bala", "portocala"});
            jpio.write(new double[][]{{1,2,3,9.99},{-9.876,3.12,45,89}});

            jpio.write(0);
        }

        try (InputStream in = jpio.getIn()) {
            System.out.println(jpio.readInt());
            System.out.println(jpio.readDouble());
            System.out.println(Arrays.toString(jpio.readIntArray()));
            System.out.println(jpio.readString());
            System.out.println(Arrays.toString(jpio.readDoubleArray()));
            System.out.println(Arrays.toString(jpio.readStringArray()));
            double[][] x = jpio.readMatrix();
            for (double[] v:x){
                System.out.println(Arrays.toString(v));
            }
        }
    }


    @Override
    public void close() throws IOException {
        OutputStream out = jpio.getOut();
        InputStream in = jpio.getIn();
        if (out != null) {
            out.close();
        }
        if (in != null) {
            in.close();
        }
        if (p != null) {
            p.destroy();
        }
    }
}
