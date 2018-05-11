package pro.prieran.misis.gs.ftp;

import javafx.util.Pair;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Ужасный код, полностью выполнивший свою задачу -- быть сданным в качестве лабы
 */
public class FtpServer {
    private final static int DEFAULT_PORT = 21;

    private final static String DEFAULT_USERNAME = "some_username";
    private final static String DEFAULT_PASSWORD = "some_password";

    private final Map<String, List<Byte>> loadedFiles = new HashMap<>();

    private DataOutputStream infoOutputStream;
    private BufferedReader infoInputStream;

    private DataOutputStreamWrapper dataOutputStreamWrapper;
    private Socket infoSocket;

    private String currentDir = "/";

    public FtpServer() throws IOException {
        init();
    }

    private void init() throws IOException {
        new Thread(() -> {
            try {
                infoSocket = new ServerSocket(DEFAULT_PORT).accept();
                System.out.println("Client connected");
                infoOutputStream = new DataOutputStream(infoSocket.getOutputStream());
                infoInputStream = new BufferedReader(new InputStreamReader(infoSocket.getInputStream()));

                writeToInfoSocket("220 FTP server ready.\n");

                while (true) {
                    if (handleConnection()) {
                        return;
                    }
                }
            } catch (Throwable thr) {
                thr.printStackTrace();
            }
        }).start();
    }

    private boolean handleConnection() throws IOException, InterruptedException {
        final String command = infoInputStream.readLine();
        if (command == null) {
            System.out.println("Client is disconnected");
            if (dataOutputStreamWrapper != null) {
                dataOutputStreamWrapper.closeStream();
            }
            infoSocket.close();
            return true;
        }

        System.out.println("Get:  " + command);

        if (command.startsWith("AUTH")) {
            writeToInfoSocket("500.\n");
        } else if (command.startsWith("USER")) {
            if (command.endsWith(DEFAULT_USERNAME)) {
                writeToInfoSocket("331 User " + DEFAULT_USERNAME + " OK. Password required.\n");
            } else {
                writeToInfoSocket("530.\n");
            }
        } else if (command.startsWith("PASS")) {
            if (command.endsWith(DEFAULT_PASSWORD)) {
                writeToInfoSocket("230.\n");
            } else {
                writeToInfoSocket("530.\n");
            }
        } else if (command.startsWith("SYST")) {
            writeToInfoSocket("215 UNIX Type: L8.\n");
        } else if (command.startsWith("FEAT")) {
            writeToInfoSocket("500.\n");
        } else if (command.startsWith("PWD")) {
            writeToInfoSocket("257 \"" + (currentDir.startsWith("/") ? "" : "/") + currentDir + "\".\n");
        } else if (command.startsWith("TYPE")) {
            writeToInfoSocket("200.\n");
        } else if (command.startsWith("CWD")) {
            String newDir = command.substring(4);
            if (!newDir.equals("..")) {
                currentDir = newDir;
            } else {
                char[] curDirChars = currentDir.toCharArray();
                for (int i = curDirChars.length - 1; i >= 0; i--) {
                    if (curDirChars[i] == '/') {
                        currentDir = currentDir.substring(0, i);
                        break;
                    }
                }
            }
            writeToInfoSocket("250 OK.\n");
        } else if (command.startsWith("CDUP")) {
            currentDir = "/";
            writeToInfoSocket("200 OK.\n");
        } else if (command.startsWith("STOR")) {
            String fileName = command.substring(5);
            writeToInfoSocket("150 OK.\n");

            dataOutputStreamWrapper.waitUntilServerSocketConnected();
            DataInputStream dataInputStream = dataOutputStreamWrapper.getDataInputStream();
            ArrayList<Byte> bytes = new ArrayList<>();
            while (true) {
                try {
                    byte b = dataInputStream.readByte();
                    bytes.add(b);
                } catch (Throwable thr) {
                    break;
                }
            }

            loadedFiles.put(fileName, bytes);

            writeToInfoSocket("226 OK.\n");
        } else if (command.startsWith("RETR")) {
            String fileName = command.substring(5);

            List<Byte> loadedFile = loadedFiles.getOrDefault(fileName, null);
            if (loadedFile != null) {
                byte[] bytes = new byte[loadedFile.size()];
                for (int i = 0; i < bytes.length; i++) {
                    bytes[i] = loadedFile.get(i);
                }
                System.out.println("SEND FILE " + fileName);
                dataOutputStreamWrapper.blockingWriteToStream(bytes);
            }

            if (fileName.equals("README.txt")) {
                dataOutputStreamWrapper.blockingWriteToStream("Some text that was sent");
            } else {
                dataOutputStreamWrapper.blockingWriteToStream("Current dir is " + currentDir);
            }
            dataOutputStreamWrapper.closeStream();
            writeToInfoSocket("150 OK.\n");
            writeToInfoSocket("226 OK.\n");
        } else if (command.startsWith("PASV") || command.startsWith("EPSV")) {
            int portFirstPart = 107;
            int portSecondPart = 56;
            int port = portFirstPart * 256 + portSecondPart;

            if (dataOutputStreamWrapper != null) {
                dataOutputStreamWrapper.closeStream();
            }
            dataOutputStreamWrapper = new DataOutputStreamWrapper(port);

            writeToInfoSocket("227 Entering Passive Mode (127,0,0,1," + portFirstPart + "," + portSecondPart + ").\n");
        } else if (command.startsWith("LIST")) {
            try {
                writeToInfoSocket("150 Accepted data connection.\n");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            dataOutputStreamWrapper.blockingWriteToStream("drwx------ 1 owner group           512 Jul  7 11:35 .\n");
                            if (!"/".equals(currentDir)) {
                                dataOutputStreamWrapper.blockingWriteToStream("drwx------ 1 owner group           512 Jul  7 11:35 ..\n");
                            }

                            if (currentDir.contains("SomeFolder")) {
                                dataOutputStreamWrapper.blockingWriteToStream("-rw-r--r-- 1 owner group           213 Aug 26 16:31 CurrentFolder.txt\n");
                            } else {
                                dataOutputStreamWrapper.blockingWriteToStream("drw-r--r-- 1 owner group           213 Aug 26 16:31 SomeFolder\n");
                                dataOutputStreamWrapper.blockingWriteToStream("-rw-r--r-- 1 owner group           213 Aug 26 16:31 README.txt\n");
                                for (String s : loadedFiles.keySet()) {
                                    dataOutputStreamWrapper.blockingWriteToStream("-rw-r--r-- 1 owner group           213 Aug 26 16:31 " + s + "\n");
                                }
                            }

                            dataOutputStreamWrapper.closeStream();
                            writeToInfoSocket("226 Directory send OK.\n");
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
            writeToInfoSocket("500.\n");
        }

        //        } else if (newAnswer.startsWith("PORT")) { // Клиент открывает порт, сервер коннектится к нему
//            String[] split = newAnswer.split("[,]");
//            int port = Integer.parseInt(split[split.length - 1]);
//            new Thread(() -> {
//                try {
//                    System.out.println("DataSocket_2 is ready to create");
//                    Socket socket = new Socket("172.16.0.10", port);
//                    dataOutputStream = new DataOutputStreamWrapper(socket.getOutputStream());
//                    System.out.println("DataSocket_2 is created");
//                    writeToInfoSocket("200 PORT command successful");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }).start();
        //        } else if (newAnswer.startsWith("EPRT")) {
//            String[] split = newAnswer.split("[|]");
//            int port = Integer.parseInt(split[split.length - 1]);
//            writeToInfoSocket("227 Entering Passive Mode (127,0,0,1,21," + port + ").\n");
//            ServerSocket socket = new ServerSocket(port);
//            System.out.println("DataSocket is ready to create");
//            try {
//                dataOutputStream = new DataOutputStreamWrapper(socket.accept().getOutputStream());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.println("DataSocket is created");
        return false;
    }

    private void writeToInfoSocket(String string) throws IOException {
        System.out.print("Send: " + string);
        infoOutputStream.writeBytes(string);
        infoOutputStream.flush();
    }
}
