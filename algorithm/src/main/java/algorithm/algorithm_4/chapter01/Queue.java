package algorithm.algorithm_4.chapter01;

/**
 * 文件描述：
 *
 * @author Bo.Zhao
 * @version 3.0
 * @since 18/2/24
 */
public interface Queue<E> extends MyIterable<E> {

    /**
     * 添加一个元素
     */
    void enqueue(E e);

    /**
     * 删除最早添加的元素
     */
    E dequeue();


    boolean isEmpty();

    int size();
}
