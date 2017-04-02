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
}
