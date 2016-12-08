package chap04.rule18;

import java.util.AbstractList;
import java.util.List;

/**
 * @author Kj Nam
 * @since 2016-12-08
 */
public class AbstractSkeletalImpl {
    static List<Integer> intArrayAsList(final int[] a) {
        if (a == null) {
            throw new NullPointerException();
        }

        return new AbstractList<Integer>() {
            @Override
            public Integer get(int index) {
                return a[index];
            }

            @Override
            public Integer set(int index, Integer element) {
                int oldVal = a[index];
                a[index] = element;
                return oldVal;
            }

            @Override
            public int size() {
                return a.length;
            }

        };
    }
}
