package pers.bo.zhao.action.zookeeper;

public class Assert {

    public static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError();
        }
    }
}
