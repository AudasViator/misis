package pro.prieran.misis.ctg;

public class Main {
    public static void main(String[] args) {
        int[] i = {0, 1, 2, 1, 0};
        int[] j = {1, 2, 3, 3, 2};

        Graph graph = new Graph(i, j);
        graph.print();
    }
}
