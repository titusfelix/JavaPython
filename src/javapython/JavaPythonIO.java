package javapython;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class JavaPythonIO {
    private OutputStream out;
    private InputStream in;

    public JavaPythonIO(OutputStream out, InputStream in) {
        this.out = out; this.in = in;
    }

    public void write(int v) throws Exception {
        out.write(ByteBuffer.allocate(4).putInt(v).array());
    }

    public void write(double v) throws Exception {
        out.write(ByteBuffer.allocate(Long.BYTES).putLong(Long.reverseBytes(Double.doubleToLongBits(v))).array());
    }


    public void write(double[] v) throws Exception {
        ByteBuffer bf = ByteBuffer.allocate(4);
        out.write(bf.putInt(v.length).array());
        bf = ByteBuffer.allocate(8);
        for (int i = 0; i < v.length; i++) {
            out.write(bf.putLong(Long.reverseBytes(Double.doubleToLongBits(v[i]))).array());
            bf.rewind();
        }
    }

    public void write(int[] v) throws Exception {
        ByteBuffer bf = ByteBuffer.allocate(4);
        out.write(bf.putInt(v.length).array());
        bf.rewind();
        for (int i = 0; i < v.length; i++) {
            out.write(bf.putInt(v[i]).array());
            bf.rewind();
        }
    }

    public void write(String[] s) throws Exception {
        write(s.length);
        for (String v : s) {
            write(v);
        }
    }

    public void write(double[][] v) throws Exception {
        write(v.length);
        write(v[0].length);
        for (double[] x : v) {
            write(x);
        }
    }

    public double[][] readMatrix() throws Exception {
        int n = readInt();
        int m = readInt();
        double[][] x = new double[n][m];
        byte[] b = new byte[Double.BYTES * m];
        for (int i = 0; i < n; i++) {
            in.read(b);
            ByteBuffer bf = ByteBuffer.wrap(b);
            for (int j = 0; j < m; j++) {
                x[i][j] = Double.longBitsToDouble(Long.reverseBytes(bf.getLong()));
            }
        }
        return x;
    }

    public int[] readIntArray() throws Exception {
        int n = readInt();
        int[] v = new int[n];
        byte[] b = new byte[4 * n];
        in.read(b);
        ByteBuffer bf = ByteBuffer.wrap(b);
        for (int i = 0; i < n; i++) {
            v[i] = Integer.reverseBytes(bf.getInt());
        }
        return v;
    }

    public double[] readDoubleArray() throws Exception {
        int n = readInt();
        double[] v = new double[n];
        byte[] b = new byte[Double.BYTES * n];
        in.read(b);
        ByteBuffer bf = ByteBuffer.wrap(b);
        for (int i = 0; i < n; i++) {
            v[i] = Double.longBitsToDouble(Long.reverseBytes(bf.getLong()));
        }
        return v;
    }

    public int readInt() throws Exception {
        byte[] b = new byte[Integer.BYTES];
        in.read(b);
        return ByteBuffer.wrap(b).getInt();
    }

    public double readDouble() throws Exception{
        byte[] b = new byte[Long.BYTES];
        in.read(b);
        return Double.longBitsToDouble(Long.reverseBytes(ByteBuffer.wrap(b).getLong()));
    }

    public String[] readStringArray() throws Exception {
        int n = readInt();
        String[] labels = new String[n];
        for (int i = 0; i < n; i++) {
            labels[i] = readString();
        }
        return labels;
    }

    public String readString() throws Exception {
        byte[] b = new byte[readInt()];
        in.read(b);
        return new String(b);
    }

    public void write(String sir) throws Exception {
        write(sir.length());
        byte[] b = sir.getBytes(StandardCharsets.UTF_8);
        out.write(b);
    }

    public OutputStream getOut() {
        return out;
    }

    public InputStream getIn() {
        return in;
    }
}
