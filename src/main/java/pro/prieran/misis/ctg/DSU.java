package pro.prieran.misis.ctg;

public class DSU {
    private int[] parents;
    private int[] rank;

    public DSU(int size) {
        this.parents = new int[size];
        this.rank = new int[size];
        for (int i = 0; i < parents.length; i++) {
            makeSet(i);
        }
    }

    public void makeSet(int value) {
        parents[value] = value;
        rank[value] = 0;
    }

    public int findSet(int value) {
        if (value == parents[value]) {
            return value;
        } else {
            return parents[value] = findSet(parents[value]); // Чёт я думал, что так нельзя
        }
    }

    public void unionSets(int first, int second) {
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
