package pro.prieran.misis.ctg;

import org.jetbrains.annotations.Nullable;

import static pro.prieran.misis.ctg.ArrayUtils.newArray;

@SuppressWarnings("WeakerAccess")
public class Grapf {
    private static final int NOTHING = -1;

    private int[] fromArray; // Ребро откуда
    private int[] toArray;   // Ребро куда

    private int[] weights;   // Единственное логичное название

    private int[] head;      // Номер первой дуги (в массиве fromArray), выходящей из i-ой вершины
    private int[] nextEdge;  // Номер следующей в списке дуги, выходящей из этой же вершины (-1, если последняя)

    private int countOfEdges;

    public Grapf(int[] fromArray, int[] toArray, @Nullable int[] weights) {
        this.fromArray = fromArray;
        this.toArray = toArray;
        this.weights = weights;
        countOfEdges = fromArray.length;

        update();
    }

    public void add(int from, int to, int weight) {
        for (int k = head[from]; k != -1; k = nextEdge[k]) {
            if (toArray[k] == to) {
                return;
            }
        }

        if (countOfEdges >= fromArray.length) {
            countOfEdges = fromArray.length;
            fromArray = newArray(fromArray, fromArray.length * 2, NOTHING);
            toArray = newArray(toArray, toArray.length * 2, NOTHING);
            nextEdge = newArray(nextEdge, nextEdge.length * 2, NOTHING);
            weights = newArray(weights, weights.length * 2, NOTHING);
        }

        fromArray[countOfEdges] = from;
        toArray[countOfEdges] = to;
        weights[countOfEdges] = weight;

        addInternal(countOfEdges);

        countOfEdges++;
    }

    public void delete(int from, int to) {
        int k = head[from];
        for (; k != -1; k = nextEdge[k]) {
            if (toArray[k] == to) {
                fromArray[k] = NOTHING;
                toArray[k] = NOTHING;
                weights[k] = NOTHING;
                break;
            }
        }

        if (k != NOTHING) {
            if (head[from] == k) {
                head[from] = nextEdge[k];
                nextEdge[k] = NOTHING;
            } else {
                for (int m = head[k]; m != NOTHING; m = nextEdge[m]) {
                    if (nextEdge[m] == k) {
                        nextEdge[m] = nextEdge[k];
                    }
                }
            }
        }
    }

    public String makeGraphvizString() {
        StringBuilder graph = new StringBuilder();

        graph.append("digraph {\n");

        for (int q = 0; q < head.length; q++) {
            for (int k = head[q]; k != NOTHING; k = nextEdge[k]) {
                int begin = fromArray[k];
                int end = toArray[k];
                int weight = weights[k];

                if (begin != NOTHING && end != NOTHING) {
                    graph.append("\t\t");

                    graph.append(Integer.toString(begin));
                    graph.append(" -> ");
                    graph.append(Integer.toString(end));

                    graph
                            .append("[label=\"")
                            .append(weight)
                            .append("\",weight=\"")
                            .append(weight)
                            .append("\"]");

                    graph.append(";\n");
                }
            }
        }

        graph.append("\t}");

        return graph.toString();
    }

    private void update() {
        final int countOfNodes = getCountOfNodes();

        head = newArray(null, countOfNodes, NOTHING);
        nextEdge = newArray(null, fromArray.length, NOTHING);

        for (int k = 0; k < fromArray.length; k++) {
            if (fromArray[k] == NOTHING || toArray[k] == NOTHING) {
                return;
            }
            addInternal(k);
        }
    }

    private void addInternal(int indexInFromArray) {
        if (countOfEdges >= fromArray.length) {
            head = newArray(head, head.length, NOTHING);
        }

        int from = fromArray[indexInFromArray];
        nextEdge[indexInFromArray] = head[from];
        head[from] = indexInFromArray;
    }

    private int getCountOfNodes() {
        int m = 0;
        for (int k = 0; k < fromArray.length; k++) {
            if (fromArray[k] > m) {
                m = fromArray[k];
            }
            if (toArray[k] > m) {
                m = toArray[k];
            }
        }
        m++;
        return m;
    }

    private int[] kk = new int[42];

    public String theKruskal() {
        for (int i = 0; i < kk.length; i++) {
            kk[i] = NOTHING;
        }

        // Сортировка вставками
        for (int j = 0; j < weights.length; j++) {
            int current = weights[j];
            int i = j - 1;
            while (i > 0 && weights[i] > current) {
                weights[i + 1] = weights[i];
                fromArray[i + 1] = fromArray[i];
                toArray[i + 1] = toArray[i];
                i--;
            }
            weights[i + 1] = current;
        }

        int w = 0;
        DSU dsu = new DSU();
        // TODO, TODO, TODO, TU-TUUuUU, TU-DU-DU
        for (int k = 0; k < getCountOfNodes() && w < fromArray.length - 1; k++) {
            int i = fromArray[k];
            int j = toArray[k];

            int mI = dsu.findSet(i);
            int mJ = dsu.findSet(j);

            if (mI != mJ) {
                kk[w] = k;
                w++;
                dsu.unionSets(mI, mJ);
            }
        }

        StringBuilder graph = new StringBuilder();
        graph.append("digraph {\n");

        for (int i = 0; i < kk.length; i++) {
            if (kk[i] != NOTHING) {
                int begin = fromArray[i];
                int end = toArray[i];
                int weight = weights[i];

                graph.append("\t\t");

                graph.append(Integer.toString(begin));
                graph.append(" -> ");
                graph.append(Integer.toString(end));

                graph
                        .append("[label=\"")
                        .append(weight)
                        .append("\",weight=\"")
                        .append(weight)
                        .append("\"]");

                graph.append(";\n");
            }
        }

        graph.append("\t}");

        return graph.toString();
    }

    private static class DSU {
        private int[] parents = new int[42]; // TODO: Dynamic
        private int[] rank = new int[42];

        public DSU() {
            for (int i = 0; i < parents.length; i++) {
                makeSet(i);
            }
        }

        private void makeSet(int value) {
            parents[value] = value;
            rank[value] = 0;
        }

        private int findSet(int value) {
            if (value == parents[value]) {
                return value;
            } else {
                return parents[value] = findSet(parents[value]); // Чёт я думал, что так нельзя
            }
        }

        private void unionSets(int first, int second) {
            first = findSet(first);
            second = findSet(second);
            if (first != second) {
                if (rank[first] < rank[second]) {
                    parents[first] = second;
                    if (rank[first] == rank[second]) {
                        rank[second]++;
                    }
                } else {
                    parents[second] = first;
                    if (rank[first] == rank[second]) {
                        rank[first]++;
                    }
                }
            }
        }
    }
}
