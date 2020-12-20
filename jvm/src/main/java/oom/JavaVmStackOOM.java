package oom;

/**
 * Created by zhaobo on 2016/12/25.
 *
 * VM Args:-Xss20m
 * 在多线程的测试中，通过不断地创建线程可以产生内存溢出，但是
 * 这样的溢出异常与栈空间是否足够大没有任何关系。
 * 在64位Windows10系统中运行如下代码（-Xss20m），并没有出现内存溢出，
 * 只是造成了系统假死。
 */
public class JavaVmStackOOM {
    public static void main(String[] args) {

        while (true) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        System.out.print("");
                    }
                }
            };
            thread.start();
        }
    }
}
