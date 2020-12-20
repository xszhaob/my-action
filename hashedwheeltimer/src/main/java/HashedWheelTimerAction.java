import io.netty.util.HashedWheelTimer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * netty中实现的时间轮的超时任务执行算法，
 * 该算法不是netty首创，只是netty实现了该算法。
 */
public class HashedWheelTimerAction {

    public static void main(String[] args) throws InterruptedException {

        action();

    }


    private static void action0() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        System.out.println("before timer = " + System.currentTimeMillis());
        HashedWheelTimer timer = new HashedWheelTimer(100, TimeUnit.MILLISECONDS, 6);
        timer.newTimeout(timeout -> {
            System.out.println("task1 execute...");
            System.out.println("task1 execute finished, time = " + System.currentTimeMillis());
            latch.countDown();
        }, 1, TimeUnit.SECONDS);

        timer.newTimeout(timeout -> {
            System.out.println("task2 execute...");
            System.out.println("task2 execute finished, time = " + System.currentTimeMillis());
            latch.countDown();
        }, 1, TimeUnit.SECONDS);

        latch.await();

        timer.stop();
    }

    private static void action() {
        int tick = 17;
        int mask = 15;
        int idx = tick & mask;
        System.out.println(idx);
    }
}
