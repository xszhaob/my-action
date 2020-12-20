package oom;

/**
 * Created by zhaobo on 2016/12/25.
 * 当前的很多主流框架，如Spring，Hibernate，
 * 在对类进行增强时，都会使用到CGLib这类
 * 字节码技术，增强的类越多，就需要越大的
 * 方法区来保证动态生成的Class可以加载入内存。
 */
public class JavaMethodAreaOOM {
    public static void main(String[] args) {
        while (true) {

        }
    }
}
