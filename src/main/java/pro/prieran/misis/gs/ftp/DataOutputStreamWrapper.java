package pro.prieran.misis.gs.ftp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DataOutputStreamWrapper {

    private final Thread dataOutputThread;
    private final ServerSocket serverSocket;

    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    public DataOutputStreamWrapper(int port) throws IOException {
        System.out.println("Create new server socket, port = " + port);
        serverSocket = new ServerSocket(port);
        dataOutputThread = new Thread(() -> {
            try {
                Socket accept = serverSocket.accept();
                dataOutputStream = new DataOutputStream(accept.getOutputStream());
                dataInputStream = new DataInputStream(accept.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        dataOutputThread.start();
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public void waitUntilServerSocketConnected() throws InterruptedException {
        System.out.println("waitUntilServerSocketConnected() BEGIN");
        dataOutputThread.join();
        System.out.println("waitUntilServerSocketConnected() END");
    }

    public void blockingWriteToStream(String data) throws InterruptedException, IOException {
        System.out.println("blockingWriteToStream() called: " + data);
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
        System.out.println("closeStream() called");
        dataOutputStream.close();
        serverSocket.close();
    }
}
