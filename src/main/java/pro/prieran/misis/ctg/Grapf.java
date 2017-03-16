package pro.prieran.misis.ctg;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Grapf {
    private int[] fromArray; // Ребро откуда
    private int[] toArray;   // Ребро куда

    private int[] head;      // Номер первой дуги (в массиве fromArray), выходящей из i-ой вершины
    private int[] nextEdge;  // Номер следующей в списке дуги, выходящей из этой же вершины (-1, если последняя)

    public Grapf(int[] fromArray, int[] toArray) {
        this.fromArray = fromArray;
        this.toArray = toArray;

        update();
    }

    public void add(int from, int to) {
        int afterLast = findAfterLastPosition(fromArray);
        if (afterLast == -1) {
            afterLast = fromArray.length;
            fromArray = newArray(fromArray, fromArray.length * 2);
            toArray = newArray(toArray, toArray.length * 2);
        }
        fromArray[afterLast] = from;
        toArray[afterLast] = to;
        update();
    }

    public Graph<Number, Number> makeJungGraph() {
        final DirectedSparseMultigraph<Number, Number> graph = new DirectedSparseMultigraph<>();
        for (int q = 0; q < head.length; q++) {
            graph.addVertex(q);
            for (int k = head[q]; k != -1; k = nextEdge[k]) {
                int begin = fromArray[k];
                int end = toArray[k];
                System.out.println(begin + ";" + end);

                graph.addEdge(k, begin, end, EdgeType.DIRECTED);
            }
        }
        return graph;
    }

    private void update() {
        int countOfNodes = getCountOfNodes();

        head = newArray(null, countOfNodes);
        nextEdge = newArray(null, fromArray.length);

        for (int k = 0; k < fromArray.length; k++) {
            if (fromArray[k] == -1 || toArray[k] == -1) {
                return;
            }
            int from = fromArray[k];
            nextEdge[k] = head[from];
            head[from] = k;
        }
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
            newArray[i] = -1;
        }
        if (oldArray != null) {
            System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        }
        return newArray;
    }

    private int findAfterLastPosition(@NotNull int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == -1) {
                return i;
            }
        }
        return -1;
    }
}
