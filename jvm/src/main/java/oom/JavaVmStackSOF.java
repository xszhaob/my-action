package oom;

/**
 * Created by zhaobo on 2016/12/25.
 * VM Args：-Xss128k
 */
public class JavaVmStackSOF {
    private int stackLength = 1;

    public void stackLeak() {
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) throws Throwable {
        JavaVmStackSOF oom = new JavaVmStackSOF();
        try {
            oom.stackLeak();
        } catch (Throwable e) {
            System.out.println("stack length = " + oom.stackLength);
            throw e;
        }
    }
}
