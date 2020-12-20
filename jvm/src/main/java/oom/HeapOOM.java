package oom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaobo on 2016/12/25.
 * VM Args:-Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 * Java堆内存的OOM异常是实际应用中常见的内存溢出情况。
 * 当出现Java堆内存溢出情况时，异常堆栈信息“java.lang.OutOfMemoryError”
 * 会跟着进一步提示“Java heap space”。
 * 要解决这个区域的异常，一般手段是先通过内存映射工具（如Java visualVM）对
 * Dump出来的堆存储快照进行分析，重点是通过内存中的对象是否是必要的，
 * 也就是要先分清楚到底是出现了内存泄露（Memory Leak）还是内存溢出（Memory Overflow）。
 * 如果是内存溢出，可进一步通过工具查看泄露对象到GC Roots的引用链。于是就能找到
 * 泄露对象是通过怎样的路径与GC Roots相关联并导致垃圾收集器无法自动回收它们的。
 * 掌握了泄露对象的类型信息及GC Roots引用链信息，就能比较准确地定位出泄露代码的位置信息。
 * 如果不存在内存泄露，那么就应该检查虚拟机的堆参数，与机器物理内存对比还是否还可以调大。
 * 从代码上检查是否存在某些对象生命周期过长、持有状态时间过长的情况，重新设计代码结构以
 * 减少内存占用。
 */
public class HeapOOM {

    static class OOMObject {

    }

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<OOMObject>();
        while (true) {
            list.add(new OOMObject());
        }
    }
}
