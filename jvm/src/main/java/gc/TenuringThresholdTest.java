package gc;

/**
 * author:xszhaobo
 * <p/>
 * date:2016/12/26
 * <p/>
 * package_name:gc
 * <p/>
 * project: JVMPractices
 * VM Args:-verbose:gc -Xms20 -Xmx20 -Xmn10 -XX:+PrintGCDetails
 * -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1
 *
 * 虚拟机给每个对象定义了一个对象年龄（Age）计数器；如果对象在Eden出生
 * 并且经历一次Minor GC后仍然存货，并且能被Survivor容纳的话，
 * 将被移动到Survivor空间中，并且对象年龄设置为1。对象在Survivor中每熬过
 * 一次Minor GC，年龄就增加1岁，当它的年龄达到一定程度（默认为15岁），
 * 就将会被晋升到老年代。对象晋升到老年代的年龄阈值，可以通过参数
 * -XX:MaxTenuringThreshold设置。
 */
public class TenuringThresholdTest {
    private static final int _1M = 1024 * 1024;
    public static void main(String[] args) {
        byte[] allocation1,allocation2,allocation3;

        allocation1 = new byte[_1M/4];
        allocation2 = new byte[4 * _1M];
        allocation3 = new byte[4 * _1M];

        allocation2 = null;

        allocation3 = new byte[4 * _1M];
    }
}
