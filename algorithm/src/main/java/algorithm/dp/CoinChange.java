package algorithm.dp;

/**
 * @author zhaobo
 * @Description
 * @date 2022/9/3 19:20
 */
public class CoinChange {

    public static void main(String[] args) {
        System.out.println(coinChange(new int[]{1, 2, 5}, 11));
    }


    private static int coinChange(int[] coinClass, int m) {
        int[] f = new int[m + 1];
        int n = coinClass.length;

        // 初始条件
        f[0] = 0;

        for (int i = 1; i <= m; i++) {
            f[i] = Integer.MAX_VALUE;

            for (int j = 0; j < n; j++) {
                if (i >= coinClass[j] && f[i - coinClass[j]] != Integer.MAX_VALUE) {
                    f[i] = Math.min(f[i - coinClass[j]] + 1, f[i]);
                }
            }
        }

        if (f[m] == Integer.MAX_VALUE) {
            return -1;
        }
        return f[m];
    }
}
