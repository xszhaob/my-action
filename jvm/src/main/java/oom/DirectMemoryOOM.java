package oom;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by zhaobo on 2016/12/25.
 *
 * VM Args:-Xmx20M -XX:MaxDirectMemorySize=10M
 * DirectMemory容量可以通过-XX:MaxDirectMemorySize指定，
 * 如果不指定，则默认和Java堆最大值一样。
 *
 * 由DirectMemory导致的内存溢出，一个明显的特征是在Heap Dump
 * 文件中不会看见明显的异常，如果读者发现OOM之后Dump文件很小，
 * 而程序中又直接或间接使用了NIO，那就可以考虑检查一下
 * 是不是DirectMemory Overflow。
 */
public class DirectMemoryOOM {
    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) throws IllegalAccessException {
        /*
        该方法越过了DirectByteBuffer类，
        直接通过反射获取Unsafe实例进行内存分配（Unsafe类的getUnsafe()方法
        限制了只有引导类加载类才会返回实例，也就是设计者希望只有rt.jar中的类
        才能使用Unsafe的功能）。因为，虽然使用DirectByteBuffer分配内存也会
        抛出内存溢出异常，但它抛出异常时并没有真正向操作系统申请分配内存，
        而是通过计算得知内存无法分配，于是手动抛出异常，真正申请分配内存的方法是
        unsafe.allocateMemory()。
         */
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        while (true) {
            unsafe.allocateMemory(_1MB);
        }
    }
}
