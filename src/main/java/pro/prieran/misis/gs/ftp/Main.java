package pro.prieran.misis.gs.ftp;

import java.io.IOException;

public class Main {
    private static FtpServer ftpServer;

    public static void main(String[] args) throws IOException {
        ftpServer = new FtpServer();
    }
}
