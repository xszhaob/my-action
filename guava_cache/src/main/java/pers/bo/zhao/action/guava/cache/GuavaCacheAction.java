package pers.bo.zhao.action.guava.cache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaCacheAction {

    private static final LocalCache localCache = new LocalCache();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        while (true) {
            mockGet();
            Thread.sleep(TimeUnit.SECONDS.toMillis(2));
        }

    }

    private static void mockGet() throws ExecutionException {
        for (long i = 0; i < 100000L; i++) {
            Long value = localCache.getValue(i);
            System.out.println(value);
        }
    }
}
