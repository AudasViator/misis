package pro.prieran.misis.ctg;

public class GrapfUtils {

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

        final DSU dsu = new DSU(fromArray.length);
        final int[] sTree = new int[42];
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
}
