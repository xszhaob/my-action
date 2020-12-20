package gc;

/**
 * author:xszhaobo
 * <p/>
 * date:2016/12/25
 * <p/>
 * package_name:gc
 * <p/>
 * project: JVMPractices
 *
 * VM Args:-verbose:gc -Xms20m -Xmx20m -Xmn10m -XX:+PrintGCDetails -XX:SurvivorRatio=8
 */
public class MinorGC {
    private static final int _1M = 1024 * 1024;

    /*
    [GC[DefNew: 7307K->528K(9216K), 0.0036747 secs] 7307K->6672K(19456K), 0.0037049 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
    Heap
     def new generation   total 9216K, used 4874K [0x00000000f9a00000, 0x00000000fa400000, 0x00000000fa400000)
      eden space 8192K,  53% used [0x00000000f9a00000, 0x00000000f9e3e6e8, 0x00000000fa200000)
      from space 1024K,  51% used [0x00000000fa300000, 0x00000000fa384198, 0x00000000fa400000)
      to   space 1024K,   0% used [0x00000000fa200000, 0x00000000fa200000, 0x00000000fa300000)
     tenured generation   total 10240K, used 6144K [0x00000000fa400000, 0x00000000fae00000, 0x00000000fae00000)
       the space 10240K,  60% used [0x00000000fa400000, 0x00000000faa00030, 0x00000000faa00200, 0x00000000fae00000)
     compacting perm gen  total 21248K, used 2984K [0x00000000fae00000, 0x00000000fc2c0000, 0x0000000100000000)
       the space 21248K,  14% used [0x00000000fae00000, 0x00000000fb0ea3c8, 0x00000000fb0ea400, 0x00000000fc2c0000)
    No shared spaces configured.

    日志分析：
    执行main方法中分配allocation4对象的语句是会发生一次Minor GC，
    这次GC的结果是新生代7307K变成528K，而总内存占用量机会没有减少（因为allocation1、
    allocation2、allocation3三个对象都是存活的，虚拟机几乎没有找到可回收的对象）。
    这次GC发生原因是给allocation4分配内存的时候，发现allocation4需要4m，
    而Eden已经被占用了6M，剩余空间不足以分配allocation4所需要的4m内存，因此发生GC。
    GC期间虚拟机又发现Survivor区不足以存放3个2m大小的对象，因此通过担保机制提前
    转移到老年代区。
    GC结束之后，可以看出，Eden中存放了allocation4，大约有4m，老年代被占用6m
    （allocation1、allocation2、allocation3）。
     */
    public static void main(String[] args) {
        byte[] allocation1,allocation2,allocation3,allocation4;
        allocation1 = new byte[2 * _1M];
        allocation2 = new byte[2 * _1M];
        allocation3 = new byte[2 * _1M];
        allocation4 = new byte[4 * _1M];
    }
}
