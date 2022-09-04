package algorithm.algorithm_4.chapter04;

import algorithm.algorithm_4.chapter01.LinkedStack;

/**
 * @author Bo.Zhao
 * @since 18/9/2
 */
public abstract class Paths {

    public abstract boolean hasPathTo(int v);

    public abstract Iterable<Integer> pathTo(int v);

    protected Iterable<Integer> pathTo(int source, int[] edgeTo, int v) {
        if (!hasPathTo(v)) {
            return null;
        }
        LinkedStack<Integer> path = new LinkedStack<>();
        for (int x = v; x != source; x = edgeTo[x]) {
            path.push(x);
        }
        path.push(source);
        return path;
    }
}
