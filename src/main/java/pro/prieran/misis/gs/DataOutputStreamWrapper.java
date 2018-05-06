package pro.prieran.misis.gs;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

public class DataOutputStreamWrapper {

    private final Thread dataOutputThread;
    private final ServerSocket serverSocket;

    private DataOutputStream dataOutputStream;

    public DataOutputStreamWrapper(int port) throws IOException {
        System.out.println("Create new server socket, port = " + port);
        serverSocket = new ServerSocket(port);
        dataOutputThread = new Thread(() -> {
            try {
                dataOutputStream = new DataOutputStream(serverSocket.accept().getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        dataOutputThread.start();
    }

    public void waitUntilServerSocketConnected() throws InterruptedException {
        dataOutputThread.join();
    }

    public void blockingWriteToStream(String data) throws InterruptedException, IOException {
        waitUntilServerSocketConnected();
        dataOutputStream.writeBytes(data);
        dataOutputStream.flush();
    }

    public void blockingWriteToStream(byte[] data) throws InterruptedException, IOException {
        waitUntilServerSocketConnected();
        dataOutputStream.write(data);
        dataOutputStream.flush();
    }

    public void closeStream() throws IOException {
        dataOutputStream.close();
        serverSocket.close();
    }
}
