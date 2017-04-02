package pro.prieran.misis.ctg;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("WeakerAccess")
public class Grapf {
    private static final int NOTHING = -1;

    private int[] fromArray; // Ребро откуда
    private int[] toArray;   // Ребро куда

    private int[] weights;

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

    public void add(int from, int to) {
        for (int k = head[from]; k != -1; k = nextEdge[k]) {
            if (toArray[k] == to) {
                return;
            }
        }

        if (countOfEdges >= fromArray.length) {
            countOfEdges = fromArray.length;
            fromArray = newArray(fromArray, fromArray.length * 2);
            toArray = newArray(toArray, toArray.length * 2);
            nextEdge = newArray(nextEdge, nextEdge.length * 2);
        }

        fromArray[countOfEdges] = from;
        toArray[countOfEdges] = to;
        addEdge(countOfEdges);

        countOfEdges++;
    }

    private void addInternal(int from, int to) {
        if (countOfEdges >= fromArray.length) {
            head = newArray(head, head.length);
        }
        nextEdge[countOfEdges] = head[from];
        head[from] = countOfEdges;
    }

    public void delete(int from, int to) {
        int i = 0;
        for (; i < fromArray.length; i++) {
            if (from == fromArray[i] && to == toArray[i]) {
                fromArray[i] = NOTHING;
                toArray[i] = NOTHING;
                break;
            }
        }
        if (i != fromArray.length) {
            if (head[from] == i) {
                head[from] = nextEdge[i];
                nextEdge[i] = NOTHING;
            } else {
                for (int k = head[i]; k != NOTHING; k = nextEdge[k]) {
                    if (nextEdge[k] == i) {
                        nextEdge[k] = nextEdge[i];
                    }
                }
            }
        }
    }

    public Graph<Number, Number> makeJungGraph() {
        final DirectedSparseMultigraph<Number, Number> graph = new DirectedSparseMultigraph<>();
        for (int q = 0; q < head.length; q++) {
            graph.addVertex(q);
            for (int k = head[q]; k != NOTHING; k = nextEdge[k]) {
                int begin = fromArray[k];
                int end = toArray[k];
                System.out.println(begin + ";" + end);

                if (begin != NOTHING && end != NOTHING) {
                    graph.addEdge(k, begin, end, EdgeType.DIRECTED);
                }
            }
        }
        return graph;
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

        head = newArray(null, countOfNodes);
        nextEdge = newArray(null, fromArray.length);

        for (int k = 0; k < fromArray.length; k++) {
            if (fromArray[k] == NOTHING || toArray[k] == NOTHING) {
                return;
            }
            addEdge(k);
        }
    }

    private void addEdge(int indexInFromArray) {
        if (countOfEdges >= fromArray.length) {
            head = newArray(head, head.length);
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

    private int[] newArray(@Nullable int[] oldArray, int newCapacity) {
        int[] newArray = new int[newCapacity];
        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = NOTHING;
        }
        if (oldArray != null) {
            System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        }
        return newArray;
    }
}
