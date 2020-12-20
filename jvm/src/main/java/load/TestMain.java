package load;

/**
 * 文件描述：虚拟机会保证类的初始化在多线程环境中被正确地加锁、同步，
 * 即如果多个线程同时去初始化一个类，那么只会有一个类去执行这个类的<clinit>()方法，
 * 其他线程都要阻塞等待，直至活动线程执行<clinit>()方法完毕。
 * 因此如果在一个类的<clinit>()方法中有耗时很长的操作，就可能造成多个进程阻塞。
 * 不过其他线程虽然会阻塞，但是执行<clinit>()方法的那条线程退出<clinit>()方法后，
 * 其他线程不会再次进入<clinit>()方法了，因为同一个类加载器下，一个类只会初始化一次。
 * 实际应用中这种阻塞往往是比较隐蔽的，要小心。
 *
 * JVM对类进行初始化的5种场景之一：使用new关键字实例化对象、读取或者设置一个类的静态字段（被final修饰的静态字段除外）、
 * 调用一个类的静态方法的时候。子类引用父类静态字段，不会导致子类初始化。至于子类是否被加载、验证了，
 * 前者可以通过”-XX:+TraceClassLoading”来查看
 *
 * @author Bo.Zhao
 * @version 3.0
 * @since 17/5/23
 */
public class TestMain {
    public static void main(String[] args) {
        System.out.println(SubClass.value);
    }
}

/**
 * 文件描述：
 *
 * @author Bo.Zhao
 * @version 3.0
 * @since 17/5/23
 */
class SubClass extends SuperClass {

    static {
        System.out.println("##############Sub Class init.");
    }
}

/**
 * 文件描述：
 *
 * @author Bo.Zhao
 * @version 3.0
 * @since 17/5/23
 */
class SuperClass {
    static int value = 3;

    static {
        System.out.println("##############Super Class init");
    }
}
