package com.gmail.gazllloyd.Optimiser.util;

import java.util.Iterator;

/**
 * https://github.com/aisrael/jcombinatorics/blob/master/src/main/java/jcombinatorics/permutations/SepaPnkIterator.java
 *
 *
 *
 * <p>
 * An iterator that enumerates <code>P(n,k)</code>, or all permutations of
 * <code>n</code> taken <code>k</code> at a time in lexicographic order. Derived
 * from the SEPA P(n) iterator.
 * </p>
 *
 * @author Alistair A. Israel
 * see SepaPnIterator
 */
public class SepaPnkIterator extends ReadOnlyIterator<int[]> {

    private boolean hasNext = true;

    private final int n;

    private final int k;

    private final int[] a;

    private final int[] result;

    /**
     * @param n
     *        the number of elements
     * @param k
     *        taken k at a time
     */
    public SepaPnkIterator(final int n, final int k) {
        if (n < 1) {
            throw new IllegalArgumentException("Need at least 1 element!");
        }
        if (k < 0 || k > n) {
            throw new IllegalArgumentException("0 < k <= n!");
        }
        this.n = n;
        this.k = k;
        a = identityPermutation(n);
        result = new int[k];
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.Iterator#hasNext()
     */
    public final boolean hasNext() {
        return hasNext;
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.Iterator#next()
     */
    public final int[] next() {
        System.arraycopy(a, 0, result, 0, k);
        computeNext();
        return result;
    }

    /**
     *
     */
    private void computeNext() {
        int i = k - 1;
        int j = k;
        // find smallest j > k - 1 where a[j] > a[k - 1]
        while (j < n && a[i] >= a[j]) {
            ++j;
        }
        if (j < n) {
            swap(i, j);
        } else {
            reverseRightOf(i);
            // i = (k - 1) - 1
            --i;
            while (i >= 0 && a[i] >= a[i + 1]) {
                --i;
            }
            if (i < 0) {
                hasNext = false;
                return;
            }
            // j = n - 1
            --j;
            while (j > i && a[i] >= a[j]) {
                --j;
            }
            swap(i, j);
            reverseRightOf(i);
        }
    }

    /**
     * Reverse the order of elements from <code>a[start + 1]..a[n-1]</code>.
     *
     * @param start
     *        the starting element
     */
    private void reverseRightOf(final int start) {
        int i = start + 1;
        int j = n - 1;
        while (i < j) {
            swap(i, j);
            ++i;
            --j;
        }
    }

    /**
     * @param x
     *        first position
     * @param y
     *        second position
     */
    private void swap(final int x, final int y) {
        final int t = a[x];
        a[x] = a[y];
        a[y] = t;
    }

    /**
     *
     * @author Alistair A. Israel
     */
    public static class Factory implements Iterable<int[]> {

        private final int n;

        private final int k;

        /**
         * @param n
         *        the number of elements
         * @param k
         *        taken k at a time
         */
        public Factory(final int n, final int k) {
            if (n < 1) {
                throw new IllegalArgumentException("Need at least 1 element!");
            }
            if (n < k || k < 0) {
                throw new IllegalArgumentException("0 < k <= n!");
            }
            this.n = n;
            this.k = k;
        }

        /**
         * {@inheritDoc}
         *
         * @see Iterable#iterator()
         */
        public final Iterator<int[]> iterator() {
            return new SepaPnkIterator(n, k);
        }

    }

    public static void identityPermutation(final int[] a) {
        for (int i = a.length - 1; i >= 0; --i) {
            a[i] = i;
        }
    }
    public static int[] identityPermutation(final int n) {
        final int[] a = new int[n];
        identityPermutation(a);
        return a;
    }
}

abstract class ReadOnlyIterator<T> implements Iterator<T> {

    /**
     * {@value #REMOVE_OPERATION_NOT_SUPPORTED_MESSAGE}.
     */
    public static final String REMOVE_OPERATION_NOT_SUPPORTED_MESSAGE = "remove() operation not supported!";

    /**
     * Not supported. Throws {@link UnsupportedOperationException} exception.
     */
    public final void remove() {
        throw new UnsupportedOperationException(REMOVE_OPERATION_NOT_SUPPORTED_MESSAGE);
    }

}
