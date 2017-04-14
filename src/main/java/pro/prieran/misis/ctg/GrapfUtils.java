package pro.prieran.misis.ctg;

import java.util.Locale;

public class GrapfUtils {
    private static final int UNREACH = -2;

    public static String theBFS(Grapf grapf, int fromNode) {
        final int[] lengths = new int[grapf.countOfEdges]; // Растояния до исходной вершины от i-ой вершины
        final int[] parents = ArrayUtils.newArray(null, grapf.countOfNodes, UNREACH); // Откуда пришли в i-ую вершину (хранится номер дуги)

        final int[] queue = new int[grapf.countOfEdges];
        queue[0] = fromNode;

        int firstIndex = 0; // First element in queue
        int firstEmptyIndex = 1; // First empty space in queue

        while (firstIndex < firstEmptyIndex) {
            int from = queue[firstIndex];
            firstIndex++;
            for (int k = grapf.head[from]; k != Grapf.NOTHING; k = grapf.nextEdge[k]) {
                int to = grapf.toArray[k];
                if (lengths[to] == 0) {
                    lengths[to] = lengths[from] + grapf.weights[k];
                    parents[to] = k;
                    queue[firstEmptyIndex] = to;
                    firstEmptyIndex++;
                }
            }
        }

        int maxLength = 0;
        for (int length : lengths) {
            if (length > maxLength) {
                maxLength = length;
            }
        }

        /*
            1. Если иду из четвёртой в восьмую
		        a. Известно расстояние из 4 до 8 (пусть оно равно 100)
		        b. Известно, по каким рёбрам я шёл
		        c. Известны веса этих рёбер
	        2. Цель:
		        a. Найти самое большое расстояние от четвёртой вершины до некой наиболее отдалённой
		        b. Идя из четвёртого ребра в ширину, раскрашивать каждое ребро пропорционально:
			            i. (расстояние от четвёртого ребра до текущей вершины)/(самое большое расстояние)
	        3. Алгоритм:
		        a. Идём по from/toArray, имеем номер ребра
		        b. Проверяем, что мы идём из той вершины (точнее по тому ребру), из которой надо идти
		        c. По куда ребро ищем расстояние до исходной вершины
		        d. По номеру ребра ищем вес ребра
		            Градиента не будет
		        e. Градиент в начале пропорционален (расстояние до исходной - вес ребра)/(максимальное расстояние)
		        f. Градиент в конце пропорционален (расстояние до исходной)/(максимальное расстояние)
		        g. Color will be in HSV format = "0.833 1.000 0.500" - purple color; We need to change S from 1 to 0

         */

        StringBuilder graph = new StringBuilder();
        graph.append("digraph {\n");
        addGraphvizStyle(graph);

        graph.append("\tnode [color=gray, fontcolor=gray]\n");

        for (int k = 0; k < grapf.countOfNodes; k++) {
            if (parents[k] != UNREACH || fromNode == k) {
                graph.append("\t\t ").append(k).append(" [color=\"");
                if (fromNode != k) {
                    String color = makeHSV(lengths, k, maxLength);
                    graph.append(color);
                    graph.append("\", fontcolor=\"");
                    graph.append(color);
                    graph.append("\"]\n");
                } else {
                    graph.append("black\", shape=star, fontcolor=black]\n");
                }
            }
        }

        graph.append("\n");

        for (int k = 0; k < grapf.fromArray.length; k++) {
            int from = grapf.fromArray[k];
            int to = grapf.toArray[k];

            if (from != Grapf.NOTHING && to != Grapf.NOTHING) {
                graph.append("\t\t");

                graph.append(Integer.toString(from));
                graph.append(" -> ");
                graph.append(Integer.toString(to));

                graph.append(" [");

                if (parents[to] == k) {
                    graph
                            .append("label=\"")
                            .append(lengths[to])
                            .append("\", weight=\"")
                            .append(grapf.weights[k])
                            .append("\"");

                    graph.append(", color = \"");
                    graph.append(makeHSV(lengths, to, maxLength));
                    graph.append("\"");

                    graph.append(", fontcolor = \"");
                    graph.append(makeHSV(lengths, to, maxLength));
                    graph.append("\"");
                } else {
                    graph
                            .append("label=\"")
                            .append("+")
                            .append(grapf.weights[k])
                            .append("\", weight=\"")
                            .append(grapf.weights[k])
                            .append("\"");
                    graph.append(", color = gray, fontcolor = gray");
                }

                graph.append("];\n");
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

    private static String makeHSV(int[] lengths, int to, int maxLength) {
        StringBuilder color = new StringBuilder();
        float saturationF = 1 - (float) lengths[to] / (maxLength * 1.1f);
        String saturation = String.format(Locale.ENGLISH, "%.3f", saturationF);

        color.append("0.666  ");
        color.append(saturation);
        color.append("  ");
        color.append("1.000");

        return color.toString();
    }
}
