package gc;

/**
 * Created by zhaobo on 2016/12/25.
 * VM Args：-XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+PrintHeapAtGC
 *
 * 从运行结果可以看出，GC前和GC后堆内存的变化
 *  PSYoungGen used 6758K -> 696K
 * 这从侧面看出，Java 虚拟机并不是通过引用计数器算法来判断对象是否存活的。
 */
public class ReferenceCountingGC {
    public Object instance = null;
    private static final int _1M = 1024 * 1024;

    private byte[] bigSize = new byte[2 * _1M];


    public static void main(String[] args) {
        ReferenceCountingGC objA = new ReferenceCountingGC();
        ReferenceCountingGC objB = new ReferenceCountingGC();

        objA.instance = objB;
        objB.instance = objA;

        objA = null;
        objB = null;

        System.gc();
    }
}
