import java.util.ArrayList;
import java.util.List;

/**
 * 运行时需加载的虚拟机参数：
 * <p>
 * -XX:+PrintGCDetails
 * -XX:+PrintGCDateStamps
 * -XX:+PrintGCTimeStamps
 * -XX:+PrintFlagsFinal
 */
public class JvmAction {

    private static final int K = 1024;
    private static final int M = 1024 * K;

    public static void main(String[] args) throws InterruptedException {
        List<byte[]> list = new ArrayList<>();

        for (int i = 0; i < 500; i++) {
            Thread.sleep(10);
            list.add(new byte[M]);
        }

        for (byte[] bytes : list) {
            System.out.println(bytes.length);
        }

        System.out.println(f(123, 0));
    }

    private static int f(int x, int y) {
        return x == 0 ? y : (f(x / 10, y * 10 + x % 10));
    }
}
