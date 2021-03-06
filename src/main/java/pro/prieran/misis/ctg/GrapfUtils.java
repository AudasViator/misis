package pro.prieran.misis.ctg;

import java.util.Locale;

public class GrapfUtils {
    private static final int INFINITY = Integer.MAX_VALUE;

    private static final int NO_PARENT = -1;
    private static final int UNREACHABLE = -2;

    private static final int OUTSIDE = -2;
    private static final int LAST_NODE = -1;

    private static final int EMPTY_COMPONENT = -1;

    private static final int NO_WAY = -42;

    public static String theKR(int firstNode, int secondNode, int thirdNode) {

        final int countOfNodes = 30;
        final int[] fromArray = {0, 21, 15, 7, 6, 13, 28, 4, 28, 4, 20, 19, 1, 5, 22, 4, 22, 22, 16, 16, 19, 24, 0, 12, 10, 26, 21, 3, 13, 3};
        final int[] toArray = {1, 7, 6, 18, 11, 22, 16, 22, 8, 9, 21, 10, 18, 19, 15, 0, 10, 0, 3, 20, 8, 9, 5, 22, 16, 20, 15, 12, 24, 28};
        final int[] weights = {1, 16, 12, 1, 7, 14, 2, 18, 15, 10, 3, 19, 5, 8, 12, 4, 8, 12, 17, 9, 16, 19, 1, 5, 9, 1, 6, 7, 15, 17};

        final int countOfEdges = fromArray.length;

        ArrayUtils.sortArraysLikeFirst(weights, fromArray, toArray);

        final DSU dsu = new DSU(countOfEdges);
        final int[] sTree = ArrayUtils.newArray(countOfEdges, NO_WAY);
        int w = 0;

        for (int k = 0; k < countOfEdges && w < countOfNodes - 1; k++) {
            int from = fromArray[k];
            int to = toArray[k];

            int fromSet = dsu.findSet(from);
            int toSet = dsu.findSet(to);

            if (fromSet != toSet) {
                sTree[w] = k;
                w++;
                dsu.unionSets(fromSet, toSet);
            }

            int f = dsu.findSet(firstNode);
            int s = dsu.findSet(secondNode);
            int t = dsu.findSet(thirdNode);
            if (f == s && s == t) {
                break;
            }
        }

        for (int l = 0; l < countOfNodes; l++) {
            for (int m = 0; m < countOfNodes; m++) {
                int[] countOfEdgesFromNode = new int[countOfNodes];
                int[] numberOfEdgeFromNode = new int[countOfNodes];

                for (int node = 0; node < countOfNodes; node++) {
                    if ((node == firstNode || node == secondNode || node == thirdNode)) {
                        continue;
                    }

                    for (int k = 0; k < toArray.length; k++) {
                        if ((fromArray[k] == node || toArray[k] == node) && ArrayUtils.contains(sTree, k)) {
                            countOfEdgesFromNode[node]++;
                            numberOfEdgeFromNode[node] = k;
                        }
                    }
                }

                for (int node = 0; node < countOfEdgesFromNode.length; node++) {
                    if (countOfEdgesFromNode[node] == 1) {
                        for (int k = 0; k < sTree.length; k++) {
                            if (sTree[k] == numberOfEdgeFromNode[node]) {
                                sTree[k] = NO_WAY;
                            }
                        }
                    }
                }
            }
        }

        StringBuilder graph = new StringBuilder();
        graph.append("digraph {\n");
        addGraphvizStyle(graph);

        for (int k = 0; k < countOfEdges; k++) {
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

        graph.append("\t}");

        return graph.toString();
    }

    public static String theDFS(Grapf grapf) {
        if (!grapf.isBiDirectional) {
            throw new IllegalStateException("Graph is not bidirectional");
        }

        int[] stack = new int[grapf.countOfNodes];
        int[] numbersOfComponent = ArrayUtils.newArray(grapf.countOfNodes, -1);
        int[] numbersOfEdge = ArrayUtils.newArray(grapf.head, grapf.head.length, 0);

        int firstEmptyInStack = 0;
        int numberOfComponent = -1;

        for (int q = 0; q < numbersOfComponent.length; q++) {
            if (numbersOfComponent[q] != EMPTY_COMPONENT) {
                continue;
            }

            numberOfComponent++;
            int currentNode = q;

            while (true) {
                numbersOfComponent[currentNode] = numberOfComponent;
                int k;
                int to = Grapf.NOTHING;
                for (k = numbersOfEdge[currentNode]; k != Grapf.NOTHING; k = grapf.nextEdge[k]) {
                    to = grapf.toArray[k];
                    if (numbersOfComponent[to] == EMPTY_COMPONENT) {
                        break;
                    }
                }
                if (k != Grapf.NOTHING) {
                    numbersOfEdge[currentNode] = grapf.nextEdge[k];
                    stack[firstEmptyInStack] = currentNode;
                    firstEmptyInStack++;
                    currentNode = to;
                } else {
                    if (firstEmptyInStack == 0) {
                        break;
                    } else {
                        firstEmptyInStack--;
                        currentNode = stack[firstEmptyInStack];
                    }
                }
            }
        }

        StringBuilder graph = new StringBuilder();
        graph.append("digraph {\n");
        addGraphvizStyle(graph);

        graph.append("\tnode [color=gray, fontcolor=gray]\n");

        for (int k = 0; k < grapf.countOfNodes; k++) {
            graph.append("\t\t ").append(k).append(" [color=\"");
            String color = "0." + numbersOfComponent[k] + "00  1.000  1.000";
            graph.append(color);
            graph.append("\", fontcolor=\"");
            graph.append(color);
            graph.append("\"]\n");
        }

        for (int k = 0; k < grapf.countOfEdges / 2; k++) {
            int begin = grapf.fromArray[k];
            int end = grapf.toArray[k];
            int weight = grapf.weights[k];

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

                graph.append("];\n");
            }
        }

        graph.append("\t}");

        return graph.toString();
    }

    public static String theDijkstra(Grapf grapf, int fromNode) {
        final int[] lengths = ArrayUtils.newArray(grapf.countOfNodes, INFINITY);
        final int[] parents = ArrayUtils.newArray(grapf.countOfNodes, UNREACHABLE);
        lengths[fromNode] = 0;
        parents[fromNode] = NO_PARENT;

        int maxLength = 0;
        for (int i = 0; i < grapf.weights.length; i++) {
            if (grapf.weights[i] > maxLength) {
                maxLength = grapf.weights[i];
            }
        }

        int countOfBuckets = grapf.countOfNodes * maxLength;
        Bucket bucket = new Bucket(countOfBuckets, grapf.countOfEdges);
        bucket.insert(fromNode, 0);

        for (int b = 0; b < countOfBuckets; b++) {
            int i;
            while ((i = bucket.get(b)) != Bucket.NOTHING)
                for (int k = grapf.head[i]; k != Grapf.NOTHING; k = grapf.nextEdge[k]) {
                    int toNode = grapf.toArray[k];
                    int toLength = lengths[toNode];

                    if (lengths[i] + grapf.weights[k] < toLength) {
                        lengths[toNode] = lengths[i] + grapf.weights[k];
                        parents[toNode] = k;
                        if (toLength != INFINITY) {
                            bucket.remove(toNode, toLength);
                        }
                        bucket.insert(toNode, lengths[toNode]);
                    }
                }
        }

        return makeGraphvizLengthsTree(grapf, fromNode, lengths, parents);
    }

    public static String theBellmanFord(Grapf grapf, int fromNode) {
        final int[] lengths = ArrayUtils.newArray(grapf.countOfNodes, INFINITY);
        final int[] parents = ArrayUtils.newArray(grapf.countOfNodes, UNREACHABLE);
        final int[] queue = ArrayUtils.newArray(grapf.countOfNodes, OUTSIDE);

        int headQueue = fromNode;
        int tallQueue = fromNode;
        queue[fromNode] = LAST_NODE;

        lengths[fromNode] = 0;
        parents[fromNode] = NO_PARENT;

        while (headQueue != LAST_NODE) {
            int currentNode = headQueue;
            headQueue = queue[headQueue];
            queue[currentNode] = OUTSIDE;

            for (int k = grapf.head[currentNode]; k != Grapf.NOTHING; k = grapf.nextEdge[k]) {
                int toNode = grapf.toArray[k];
                int toLength = lengths[toNode];
                if (lengths[currentNode] + grapf.weights[k] < toLength) {
                    lengths[toNode] = lengths[currentNode] + grapf.weights[k];
                    parents[toNode] = k;

                    if (queue[toNode] == OUTSIDE) {
                        if (toLength == INFINITY) {
                            if (headQueue != LAST_NODE) {
                                queue[tallQueue] = toNode;
                            } else {
                                headQueue = toNode;
                            }
                            tallQueue = toNode;
                            queue[toNode] = LAST_NODE;
                        } else {
                            queue[toNode] = headQueue;
                            if (headQueue == LAST_NODE) {
                                tallQueue = toNode;
                                headQueue = toNode;
                            }
                        }
                    }
                }
            }
        }

        return makeGraphvizLengthsTree(grapf, fromNode, lengths, parents);
    }

    public static String theBFS(Grapf grapf, int fromNode) {
        final int[] lengths = new int[grapf.countOfEdges]; // Растояния до исходной вершины от i-ой вершины
        final int[] parents = ArrayUtils.newArray(grapf.countOfNodes, UNREACHABLE); // Откуда пришли в i-ую вершину (хранится номер дуги)

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
                    lengths[to] = lengths[from] + 1;
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

        StringBuilder graph = new StringBuilder();
        graph.append("digraph {\n");
        addGraphvizStyle(graph);

        graph.append("\tnode [color=gray, fontcolor=gray]\n");

        for (int k = 0; k < grapf.countOfNodes; k++) {
            if (parents[k] != UNREACHABLE || fromNode == k) {
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
                            .append(lengths[to])
                            .append("\"");

                    graph.append(", color = \"");
                    graph.append(makeHSV(lengths, to, maxLength));
                    graph.append("\"");

                    graph.append(", fontcolor = \"");
                    graph.append(makeHSV(lengths, to, maxLength));
                    graph.append("\"");
                } else {
                    graph.append("color = gray, fontcolor = gray");
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

        int realCountOfEdges = grapf.countOfEdges / 2;
        final int[] fromArray = ArrayUtils.newArray(grapf.fromArray, realCountOfEdges, Grapf.NOTHING);
        final int[] toArray = ArrayUtils.newArray(grapf.toArray, realCountOfEdges, Grapf.NOTHING);
        final int[] weights = ArrayUtils.newArray(grapf.weights, realCountOfEdges, Grapf.NOTHING);

        ArrayUtils.sortArraysLikeFirst(weights, fromArray, toArray);

        final DSU dsu = new DSU(grapf.countOfEdges);
        final int[] sTree = new int[grapf.countOfEdges];
        int w = 0;

        for (int k = 0; k < realCountOfEdges && w < grapf.countOfNodes - 1; k++) {
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

        StringBuilder graph = new StringBuilder();
        graph.append("digraph {\n");
        addGraphvizStyle(graph);

        for (int k = 0; k < realCountOfEdges; k++) {
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

    private static String makeGraphvizLengthsTree(Grapf grapf, int fromNode, int[] lengths, int[] parents) {
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

        int maxLength = 0;
        for (int length : lengths) {
            if (length > maxLength || length != INFINITY) {
                maxLength = length;
            }
        }

        StringBuilder graph = new StringBuilder();
        graph.append("digraph {\n");
        addGraphvizStyle(graph);

        graph.append("\tnode [color=gray, fontcolor=gray]\n");

        for (int k = 0; k < grapf.countOfNodes; k++) {
            if (parents[k] != UNREACHABLE || fromNode == k) {
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

                    graph.append(", color=\"");
                    graph.append(makeHSV(lengths, to, maxLength));
                    graph.append("\"");

                    graph.append(", fontcolor=\"");
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
