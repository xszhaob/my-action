package gc;

/**
 * Created by zhaobo on 2016/12/25.
 * JVM 进行GC之前，会调用并仅仅调用一次对象的finalize()方法。
 * 因此该实验的两段代码虽然一样，
 * 但是第一次执行和第二次执行产生的结果并不一样。
 */
public class FinalizeEscapeGC {
    private static FinalizeEscapeGC SAVE_HOOK = null;

    private void isAlive() {
        System.out.println("yes, i am still alive :)");
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize method executed！");
        FinalizeEscapeGC.SAVE_HOOK = this;
    }

    public static void main(String[] args) throws InterruptedException {
        SAVE_HOOK = new FinalizeEscapeGC();

        SAVE_HOOK = null;
        System.gc();

        Thread.sleep(500);

        if (SAVE_HOOK != null) {
            SAVE_HOOK.isAlive();
        } else {
            System.out.println("no, i am dead :(");
        }

        SAVE_HOOK = null;
        System.gc();
        Thread.sleep(500);
        if (SAVE_HOOK != null) {
            SAVE_HOOK.isAlive();
        } else {
            System.out.println("no, i am dead :(");
        }
    }
}
