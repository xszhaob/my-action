package algorithm.algorithm_4.chapter01;

import java.util.Iterator;

/**
 * Created by zhaobo on 2018/2/22.
 */
public class LinkedStack<E> implements Stack<E> {

    private Node first;
    private int n;

    @Override
    public boolean isEmpty() {
        return n == 0;
    }

    @Override
    public int size() {
        return n;
    }

    @Override
    public void push(E e) {
        Node oldFirst = first;
        first = new Node();
        first.item = e;
        first.next = oldFirst;
        n++;
    }

    @Override
    public E pop() {
        E e = first.item;
        first = first.next;
        n--;
        return e;
    }

    @Override
    public E peek() {
        Node f = first;
        return f == null ? null : f.item;
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedStackIterator();
    }

    private class Node {
        private E item;

        private Node next;
    }


    private class LinkedStackIterator implements Iterator<E> {
        private Node currentNode = first;

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public E next() {
            E item = currentNode.item;
            currentNode = currentNode.next;
            return item;
        }

        @Override
        public void remove() {

        }
    }

    @Override
    public String toString() {
        return string();
    }


    public static void main(String[] args) {
        LinkedStack<String> linkedStack = new LinkedStack<String>();
        linkedStack.push("it");
        linkedStack.push("was");
        linkedStack.push("-");
        linkedStack.push("the");
        linkedStack.push("best");
        linkedStack.push("-");
        linkedStack.push("of");
        linkedStack.push("times");
        linkedStack.push("-");
        linkedStack.push("-");
        linkedStack.push("-");
        linkedStack.push("it");
        linkedStack.push("was");
        linkedStack.push("-");
        linkedStack.push("the");
        linkedStack.push("-");
        linkedStack.push("-");
        for (String s : linkedStack) {
            System.out.println(s);
        }
    }
}
