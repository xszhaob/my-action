package oom;

/**
 * Created by zhaobo on 2016/12/25.
 * VM Args:-XX:PermSize=10m -XX:MaxPermSize=10m
 * 该代码在JDK1.6版本中可以造成内存溢出，但是在JDK1.7的版本中则不会。
 */
public class RuntimeConstantPoolOOM {
    public static void main(String[] args) {
        /*List<String> list = new ArrayList<String>();
        int i = 0;
        while(true) {
            list.add(String.valueOf(i).intern());
        }*/


        /*
        这是一个关于字符串常量池的实现问题。
        不会的JDK版本返回的结果并不相同。
         */
        String str1 = new StringBuilder("计算机").append("软件").toString();
        System.out.println(str1.intern() == str1);

        String str2 = new StringBuilder("ja").append("va").toString();
        System.out.println(str2.intern() == str2);
    }
}
