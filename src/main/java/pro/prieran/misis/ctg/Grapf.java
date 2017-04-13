package pro.prieran.misis.ctg;

import org.jetbrains.annotations.Nullable;

import static pro.prieran.misis.ctg.ArrayUtils.newArray;

@SuppressWarnings("WeakerAccess")
public class Grapf {
    private static final int NOTHING = -1;

    private final boolean isBiDirectional;

    private int[] fromArray; // Ребро откуда

    private int[] toArray;   // Ребро куда

    private int[] weights;   // Единственное логичное название
    private int[] head;      // Номер первой дуги (в массиве fromArray), выходящей из i-ой вершины
    private int[] nextEdge;  // Номер следующей в списке дуги, выходящей из этой же вершины (-1, если последняя)

    private int countOfEdges;

    public Grapf(int[] fromArray, int[] toArray, @Nullable int[] weights, boolean isBiDirectional) {
        this.fromArray = fromArray;
        this.toArray = toArray;
        this.weights = weights;
        countOfEdges = fromArray.length;
        this.isBiDirectional = isBiDirectional;

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
            if (weights != null) {
                weights = newArray(weights, weights.length * 2, NOTHING);
            }
        }

        fromArray[countOfEdges] = from;
        toArray[countOfEdges] = to;
        if (weights != null) {
            weights[countOfEdges] = weight;
        }

        addInternal(countOfEdges);

        countOfEdges++;
    }

    public void delete(int from, int to) {
        int k = head[from];
        for (; k != -1; k = nextEdge[k]) {
            if (toArray[k] == to) {
                fromArray[k] = NOTHING;
                toArray[k] = NOTHING;
                if (weights != null) {
                    weights[k] = NOTHING;
                }
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

                if (begin != NOTHING && end != NOTHING) {
                    graph.append("\t\t");

                    graph.append(Integer.toString(begin));
                    graph.append(" -> ");
                    graph.append(Integer.toString(end));

                    graph.append(" [");

                    if (isBiDirectional) {
                        graph.append("dir=both");
                    }

                    if (weights != null) {
                        if (isBiDirectional) {
                            graph.append(", ");
                        }
                        int weight = weights[k];
                        graph
                                .append("label=\"")
                                .append(weight)
                                .append("\", weight=\"")
                                .append(weight)
                                .append("\"");
                    }

                    graph.append("];\n");
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

    public String theKruskal() {
        if (!isBiDirectional) {
            throw new IllegalStateException("Graph is not bidirectional");
        }

        if (weights == null) {
            throw new IllegalStateException("Weights is null");
        }

        final int[] fromArray = ArrayUtils.newArray(this.fromArray, this.fromArray.length, NOTHING);
        final int[] toArray = ArrayUtils.newArray(this.toArray, this.toArray.length, NOTHING);
        final int[] weights = ArrayUtils.newArray(this.weights, this.weights.length, NOTHING);

        ArrayUtils.sortArraysLikeFirst(weights, fromArray, toArray);

        final DSU dsu = new DSU(fromArray.length);
        final int[] sTree = new int[42];
        int w = 0;

        for (int k = 0; k < fromArray.length && w < getCountOfNodes() - 1; k++) {
            int i = fromArray[k];
            int j = toArray[k];

            int mI = dsu.findSet(i);
            int mJ = dsu.findSet(j);

            if (mI != mJ) {
                sTree[w] = k;
                w++;
                dsu.unionSets(mI, mJ);
            }
        }

        // Print it

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

                    graph.append(" [dir=both, ");

                    graph
                            .append("label=\"")
                            .append(weight)
                            .append("\", weight=\"")
                            .append(weight)
                            .append("\"");

                    if (ArrayUtils.contains(sTree, k)) {
                        graph.append(", color = red, ");
                    }

                    graph.append("];\n");
                }
            }
        }

        graph.append("\t}");

        return graph.toString();
    }
}
