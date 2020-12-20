package gc;

/**
 * author:xszhaobo
 * <p/>
 * date:2016/12/26
 * <p/>
 * package_name:gc
 * <p/>
 * project: JVMPractices
 */
public class TenuringThreshold2Test {
    private static final int _1M = 1024 * 1024;
    public static void main(String[] args) {
        byte[] allocation1,allocation2,allocation3,allocation4;

        allocation1 = new byte[_1M/4];
//        allocation2 = new byte[_1M/4];
        allocation3 = new byte[4 * _1M];
        allocation4 = new byte[4 * _1M];

        allocation2 = null;

        allocation4 = new byte[4 * _1M];
    }
}
