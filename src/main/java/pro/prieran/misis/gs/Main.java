package pro.prieran.misis.gs;

import java.io.IOException;

public class Main {
    private static FtpServer ftpServer;

    public static void main(String[] args) throws IOException {
        ftpServer = new FtpServer();
    }
}
