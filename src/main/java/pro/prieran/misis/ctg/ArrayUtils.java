package pro.prieran.misis.ctg;

import org.jetbrains.annotations.Nullable;

public class ArrayUtils {

    private ArrayUtils() {
    }

    public static int[] newArray(@Nullable int[] oldArray, int newCapacity, int defaultValue) {
        int[] newArray = new int[newCapacity];
        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = defaultValue;
        }
        if (oldArray != null) {
            if (newArray.length < oldArray.length) {
                System.arraycopy(oldArray, 0, newArray, 0, newArray.length);
            } else {
                System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
            }
        }
        return newArray;
    }

    public static void sortArraysLikeFirst(int[]... arrays) {
        heapSort(arrays);
    }

    private static void insertionSort(int[]... arrays) {
        for (int j = 0; j < arrays[0].length; j++) {
            int current = arrays[0][j];
            int i = j - 1;
            while (i > 0 && arrays[0][i] > current) {
                swap(i, i + 1, arrays);
                i--;
            }
            arrays[0][i + 1] = current;
        }
    }

    private static void heapSort(final int[]... arrays) {
        final int arrayLength = arrays[0].length;

        for (int i = arrayLength / 2 - 1; i > -1; i--) {
            shiftDown(i, arrayLength, arrays);
        }

        int heapSize = arrayLength;
        while (heapSize > 1) {
            heapSize--;

            swap(0, heapSize, arrays);

            shiftDown(0, heapSize, arrays);
        }
    }

    /**
     * @param heapLength Because there was some reasons {@link #heapSort}
     */
    private static void shiftDown(int nElement, int heapLength, final int[]... heaps) {
        int nMax = nElement;
        int element = heaps[0][nElement];

        while (true) {
            final int nLeftChild = nElement * 2 + 1;
            if (nLeftChild < heapLength && heaps[0][nLeftChild] > element) {
                nMax = nLeftChild;
            }

            final int nRightChild = nElement * 2 + 2;
            if (nRightChild < heapLength && heaps[0][nRightChild] > heaps[0][nMax]) {
                nMax = nRightChild;
            }

            if (nMax == nElement) {
                break;
            }

            swap(nElement, nMax, heaps);

            nElement = nMax;
        }
    }

    private static void swap(int first, int second, int[]... arrays) {
        for (int[] array : arrays) {
            final int temp = array[first];
            array[first] = array[second];
            array[second] = temp;
        }
    }

    public static boolean contains(int[] array, int element) {
        for (int i : array) {
            if (i == element) {
                return true;
            }
        }
        return false;
    }
}
