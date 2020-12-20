package gc;

/**
 * author:xszhaobo
 * <p/>
 * date:2016/12/26
 * <p/>
 * package_name:gc
 * <p/>
 * project: JVMPractices
 *
 * VM Args:-verbose:gc -Xms20m -Xmx20m -Xmn10m -XX:+UseSerialGC -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=3145728
 *
 * 大对象直接进入老年代，所谓大对象是指需要大量连续内存空间的Java对象。
 * 最典型的大对象就是很长的字符串以及数组。
 * 虚拟机提供了一个-XX:PretenureSizeThreshold参数，
 * 让大于该参数的对象直接进入老年代分配。
 */
public class PretenureSizeTHresholdTest {
    private static final int _1M = 1024 * 1024;

    public static void main(String[] args) {
        byte[] allocation = new byte[4 * _1M];
    }
}
