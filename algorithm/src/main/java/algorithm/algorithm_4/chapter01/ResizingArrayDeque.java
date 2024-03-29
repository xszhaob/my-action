package algorithm.algorithm_4.chapter01;

import java.util.Iterator;

/**
 * Created by zhaobo on 2018/2/28.
 */
@SuppressWarnings("unchecked")
public class ResizingArrayDeque<E> implements Deque<E> {

    private E[] arr = (E[]) new Object[2];
    private int first, last, n;

    @Override
    public int size() {
        return n;
    }

    @Override
    public boolean isEmpty() {
        return n == 0;
    }

    @Override
    public void pushLeft(E item) {
        if (n == arr.length) {
            resize(arr.length * 2);
        }
        if (isEmpty()) {
            last = 1;
            first = 0;
        } else {
            if (first == 0) {
                first = arr.length - 1;
            } else {
                first--;
            }
        }
        arr[first] = item;
        n++;
    }

    @Override
    public void pushRight(E item) {
        if (n == arr.length) {
            resize(arr.length * 2);
        }
        arr[last] = item;
        if (n == 0) {
            first = last;
        }
        if (last == arr.length - 1) {
            last = 0;
        } else {
            last++;
        }
        n++;
    }

    @Override
    public E popLeft() {
        if (n == 0) {
            return null;
        }
        E item = arr[first];
        arr[first] = null;
        if (n == 1) {
            first = last = 0;
        } else {
            if (first == arr.length - 1) {
                first = 0;
            } else {
                first++;
            }
        }
        n--;
        return item;
    }

    @Override
    public E popRight() {
        if (isEmpty()) {
            return null;
        }
        int index = last == 0 ? arr.length - 1 : last - 1;

        if (n == 1) {
            first = last = 0;
        } else {
            if (last == 0) {
                last = arr.length - 1;
            } else {
                last--;
            }
        }
        E item = arr[index];
        arr[index] = null;
        n--;
        return item;
    }

    @Override
    public Iterator<E> iterator() {
        return new ResizingArrayDequeIterator();
    }

    private void resize(int length) {
        E[] newArr = (E[]) new Object[length];
        for (int i = 0; i < n; i++) {
            newArr[i] = arr[(first + i) % arr.length];
        }
        arr = newArr;
        first = 0;
        last = n;
    }

    private class ResizingArrayDequeIterator implements Iterator<E> {
        int fromIndex = first;
        int count = n;

        @Override
        public boolean hasNext() {
            return count > 0;
        }

        @Override
        public E next() {
            E item = arr[fromIndex];
            fromIndex = ++fromIndex % arr.length;
            count--;
            return item;
        }
    }

    @Override
    public String toString() {
        return string();
    }
}
