package pro.prieran.misis.ctg;

public class Graph {
    private int[] i; // Ребро откуда
    private int[] j; // Ребро куда

    private int[] h; // Номер первой дуги (в массиве i), выходящей из i-ой вершины
    private int[] l; // Номер следующей в списке дуги, выходящей из этой же вершины (-1, если последняя)

    public Graph(int[] i, int[] j) {
        this.i = i;
        this.j = j;

        update();
    }

    public void print() {
        for (int q = 0; q < getCountOfNodes(); q++) {
            System.out.println(q + " node");
            for (int k = h[q]; k != -1; k = l[k]) {
                int begin = i[k];
                int end = j[k];
                System.out.println(begin + ";" + end);
            }
        }
    }

    private void update() {
        int m = getCountOfNodes();

        h = new int[m];
        for (int k = 0; k < h.length; k++) {
            h[k] = -1;
        }

        l = new int[i.length];
        for (int k = 0; k < l.length; k++) {
            l[k] = -1;
        }

        for (int k = 0; k <= m; k++) {
            int from = i[k];
            l[k] = h[from];
            h[from] = k;
        }
    }

    private int getCountOfNodes() {
        int m = 0;
        for (int k = 0; k < i.length; k++) {
            if (i[k] > m) {
                m = i[k];
            }
            if (j[k] > m) {
                m = j[k];
            }
        }
        m++;
        return m;
    }
}
