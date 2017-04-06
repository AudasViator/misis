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

        int[] i = {0, 1, 2, 1, 0, 0};
        int[] j = {1, 2, 3, 3, 2, 3};
        int[] weights = {1, 2, 3, 4, 5, 6};

        Grapf grapf = new Grapf(i, j, weights);

        String graph = grapf.makeGraphvizString();
        System.out.println(graph);
        String theKruskal = grapf.theKruskal();
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

        return new Grapf(i, j, null);
    }
}