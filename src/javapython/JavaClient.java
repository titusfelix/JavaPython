package javapython;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class JavaClient implements Closeable {
    protected Socket socket;
    protected JavaPythonIO jpio;
    public JavaClient() {
    }

    public void start() throws Exception{
        socket = new Socket("127.0.0.1", 65432);
        jpio = new JavaPythonIO(socket.getOutputStream(),socket.getInputStream());
    }

    @Override
    public void close() throws IOException {
        System.out.println("--->");
        if (socket!=null){
            socket.close();
        }
        InputStream in = jpio.getIn();
        OutputStream out = jpio.getOut();
        if (in!=null){
            in.close();
        }
        if (out!=null){
            out.close();
        }
    }
}
