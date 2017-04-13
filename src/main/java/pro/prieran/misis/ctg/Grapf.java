package pro.prieran.misis.ctg;

import org.jetbrains.annotations.Nullable;

import static pro.prieran.misis.ctg.ArrayUtils.newArray;

@SuppressWarnings("WeakerAccess")
public class Grapf {
    public static final int NOTHING = -1;

    // Обойдёмся без геттеров
    public final boolean isBiDirectional;

    public int[] fromArray;  // Ребро откуда

    public int[] toArray;    // Ребро куда

    public int[] weights;    // Единственное логичное название
    public int[] head;       // Номер первой дуги (в массиве fromArray), выходящей из i-ой вершины
    public int[] nextEdge;   // Номер следующей в списке дуги, выходящей из этой же вершины (-1, если последняя)

    public int countOfEdges; // Рёбра/дуги/палки
    public int countOfNodes; // Вершины (кружочки с цифрами)

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
        if (from > countOfNodes - 1) {
            countOfNodes = from + 1;
        }

        if (to > countOfNodes - 1) {
            countOfNodes = to + 1;
        }
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

    private void update() {
        updateCountOfNodes();
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

    private void updateCountOfNodes() {
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
        countOfNodes = m;
    }
}
