package pro.prieran.misis.ctg;

import java.util.Locale;

public class GrapfUtils {
    private static final int UNREACH = -2;

    public static String theBFS(Grapf grapf, int fromNode) {
        final int[] lengths = new int[grapf.countOfEdges]; // Растояния до исходной вершины от i-ой вершины
        final int[] parents = new int[grapf.countOfNodes]; // Откуда пришли в i-ую вершину (хранится номер дуги)
        for (int i = 0; i < parents.length; i++) {
            parents[i] = UNREACH;
        }

        final int[] queue = new int[grapf.countOfEdges];
        queue[0] = fromNode;

        int firstIndx = 0; // First element in queue
        int firstEmptyIndx = 1; // First empty space in queue

        while (firstIndx < firstEmptyIndx) {
            int from = queue[firstIndx];
            firstIndx++;
            for (int k = grapf.head[from]; k != Grapf.NOTHING; k = grapf.nextEdge[k]) {
                int to = grapf.toArray[k];
                if (lengths[to] == 0) {
                    lengths[to] = lengths[from] + grapf.weights[k];
                    parents[to] = k;
                    queue[firstEmptyIndx] = to;
                    firstEmptyIndx++;
                }
            }
        }

        int maxLength = 0;
        for (int length : lengths) {
            if (length > maxLength) {
                maxLength = length;
            }
        }

        StringBuilder graph = new StringBuilder();
        graph.append("digraph {\n");
        addGraphvizStyle(graph);
        for (int q = 0; q < grapf.head.length; q++) {
            for (int k = grapf.head[q]; k != Grapf.NOTHING; k = grapf.nextEdge[k]) {
                int from = grapf.fromArray[k];
                int to = grapf.toArray[k];

                if (from != Grapf.NOTHING && to != Grapf.NOTHING) {
                    graph.append("\t\t");

                    graph.append(Integer.toString(from));
                    graph.append(" -> ");
                    graph.append(Integer.toString(to));

                    graph.append(" [");

                    graph
                            .append("label=\"")
                            .append(lengths[from])
                            .append("\", weight=\"")
                            .append(grapf.weights[k])
                            .append("\"");

                    float saturationF = 1 - (float) lengths[from] / (maxLength * 1.2f);
                    String saturation = String.format(Locale.ENGLISH, "%.3f", saturationF);

                    graph.append(", color = \"0.000  ");
                    graph.append(saturation);
                    graph.append("  ");
                    graph.append("1.000");
                    graph.append("\"");


                    graph.append("];\n");
                }
            }
        }

        graph.append("\t}");

        return graph.toString();
    }

    public static String theKruskal(Grapf grapf) {
        if (!grapf.isBiDirectional) {
            throw new IllegalStateException("Graph is not bidirectional");
        }

        if (grapf.weights == null) {
            throw new IllegalStateException("Weights is null");
        }

        final int[] fromArray = ArrayUtils.newArray(grapf.fromArray, grapf.countOfEdges, Grapf.NOTHING);
        final int[] toArray = ArrayUtils.newArray(grapf.toArray, grapf.countOfEdges, Grapf.NOTHING);
        final int[] weights = ArrayUtils.newArray(grapf.weights, grapf.countOfEdges, Grapf.NOTHING);

        ArrayUtils.sortArraysLikeFirst(weights, fromArray, toArray);

        final DSU dsu = new DSU(grapf.countOfEdges);
        final int[] sTree = new int[grapf.countOfEdges];
        int w = 0;

        for (int k = 0; k < grapf.countOfEdges && w < grapf.countOfNodes - 1; k++) {
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
        addGraphvizStyle(graph);
        for (int q = 0; q < grapf.head.length; q++) {
            for (int k = grapf.head[q]; k != Grapf.NOTHING; k = grapf.nextEdge[k]) {
                int begin = fromArray[k];
                int end = toArray[k];
                int weight = weights[k];

                if (begin != Grapf.NOTHING && end != Grapf.NOTHING) {
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

    public static String makeGraphvizString(Grapf grapf) {
        StringBuilder graph = new StringBuilder();

        graph.append("digraph {\n");
        addGraphvizStyle(graph);
        for (int q = 0; q < grapf.head.length; q++) {
            for (int k = grapf.head[q]; k != Grapf.NOTHING; k = grapf.nextEdge[k]) {
                int begin = grapf.fromArray[k];
                int end = grapf.toArray[k];

                if (begin != Grapf.NOTHING && end != Grapf.NOTHING) {
                    graph.append("\t\t");

                    graph.append(Integer.toString(begin));
                    graph.append(" -> ");
                    graph.append(Integer.toString(end));

                    graph.append(" [");

                    if (grapf.isBiDirectional) {
                        graph.append("dir=both");
                    }

                    if (grapf.weights != null) {
                        if (grapf.isBiDirectional) {
                            graph.append(", ");
                        }
                        int weight = grapf.weights[k];
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

    private static void addGraphvizStyle(StringBuilder graph) {
        graph.append("\trankdir=\"LR\"\n");
    }
}
