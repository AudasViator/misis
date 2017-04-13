package pro.prieran.misis.ctg;

import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Main {

    // TODO: Краскала с непересекающимися множествами (сжатием путей); поиск в ширину от данной вершины, выводит список дуг
    // TODO: Поиск в глубину
    // TODO: Дейкстры
    public static void main(String[] args) throws IOException {

        final int[] from = {0, 21, 15, 7, 6, 13, 28, 4, 28, 4, 20, 19, 1, 5, 22, 4, 22, 22, 16, 16, 19, 24, 0, 12, 10, 26, 21, 3, 13, 3};
        final int[] to = {1, 7, 6, 18, 11, 22, 16, 22, 8, 9, 21, 10, 18, 19, 15, 0, 10, 0, 3, 20, 8, 9, 5, 22, 16, 20, 15, 12, 24, 28};
        final int[] weights = {1, 16, 12, 1, 7, 14, 2, 18, 15, 10, 3, 19, 5, 8, 12, 4, 8, 12, 17, 9, 16, 19, 1, 5, 9, 1, 6, 7, 15, 17};

        Grapf grapf = new Grapf(from, to, weights, true);

        String theKruskal = GrapfUtils.theKruskal(grapf);
        System.out.println(theKruskal);
        writeAndRunGraph(theKruskal);
    }

    private static void writeAndRunGraph(String graph) throws IOException {
        Path path = Paths.get("graph.gv");
        BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
        writer.write(graph);
        writer.close();

//        Runtime.getRuntime().exec("explorer.exe /select," + path);
        Desktop.getDesktop().open(path.toFile());
    }

    private static Grapf parseIt() throws IOException {
        Path path = Paths.get("C:\\Users\\prieran\\YandexDisk\\МИСиС\\6семестр\\Графы\\graph.txt");

        BufferedReader reader = Files.newBufferedReader(path);
        String line = reader.readLine();
        String[] numbers = line.split(" ");

        final int countOfVertexes = Integer.parseInt(numbers[0]);
        final int countOfEdges = Integer.parseInt(numbers[1]);

        int[] i = new int[countOfEdges];
        int[] j = new int[countOfEdges];

        for (int k = 0; k < numbers.length; k++) {
            i[k] = Integer.parseInt(numbers[0]);
            j[k] = Integer.parseInt(numbers[1]);
        }

        for (int m = 0; m < countOfEdges; m++) {
            line = reader.readLine();
            numbers = line.split(" ");
            i[m] = Integer.parseInt(numbers[0]);
            j[m] = Integer.parseInt(numbers[1]);
        }

        reader.close();

        return new Grapf(i, j, null, false);
    }
}