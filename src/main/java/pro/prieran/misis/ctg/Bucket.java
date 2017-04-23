package pro.prieran.misis.ctg;

public class Bucket {
    public final static int NOTHING = -1;

    private final int[] buckets;
    private final int[] nextNodes;
    private final int[] prevNodes;

    public Bucket(int countOfBuckets, int countOfEdges) {
        buckets = ArrayUtils.newArray(countOfBuckets, NOTHING);
        nextNodes = new int[countOfEdges];
        prevNodes = new int[countOfEdges];
    }

    public void insert(int node, int numberOfBucket) {
        int nodeFromBucket = buckets[numberOfBucket];
        nextNodes[node] = nodeFromBucket;
        if (nodeFromBucket != NOTHING) {
            prevNodes[nodeFromBucket] = node;
        }
        buckets[numberOfBucket] = node;
    }

    public void remove(int node, int numberOfBucket) {
        int nextInBucket = nextNodes[node];
        int prevInBucket = prevNodes[node];

        if (node == buckets[numberOfBucket]) {
            buckets[numberOfBucket] = nextInBucket;
        } else {
            nextNodes[prevInBucket] = nextInBucket;
            if (nextInBucket != NOTHING) {
                prevNodes[nextInBucket] = prevInBucket;
            }
        }
    }

    public int get(int numberOfBucket) {
        int nodeFromBucket = buckets[numberOfBucket];
        if (nodeFromBucket != NOTHING) {
            buckets[numberOfBucket] = nextNodes[nodeFromBucket];
        }
        return nodeFromBucket;
    }
}
