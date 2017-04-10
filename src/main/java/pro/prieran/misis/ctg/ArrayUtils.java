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
            System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        }
        return newArray;
    }

    // TODO: Сортировка кучей
    public static void sortArraysLikeFirst(int[]... arrays) {
        for (int j = 0; j < arrays[0].length; j++) {
            int current = arrays[0][j];
            int i = j - 1;
            while (i > 0 && arrays[0][i] > current) {
                for (int k = 0; k < arrays.length; k++) {
                    arrays[k][i + 1] = arrays[k][i];
                }
                i--;
            }
            arrays[0][i + 1] = current;
        }
    }
}
