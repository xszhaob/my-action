package algorithm.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhaobo
 * @Description
 * @date 2023/11/8 23:34
 */
public class _131PalindromePartitioning {
    public List<List<String>> partition(String s) {
        int n = s.length();
        boolean[][] f = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(f[i], true);
        }

//        for (int i = n - 1; i >= 0; i--) {
//            for (int j = i + 1; j < n; j++) {
//                f[i][j] = s.charAt(i) == s.charAt(j) && f[i + 1][j - 1];
//            }
//        }
        for (int i = 0; i <= n - 1; i++) {
            for (int j = i - 1; j >= 0; j--) {
                f[j][i] = s.charAt(j) == s.charAt(i) && f[j + 1][i - 1];
            }
        }

        List<String> ans = new ArrayList<>();
        List<List<String>> result = new ArrayList<>();
        dfs(s, 0, result, ans, f);

        return result;
    }

    private void dfs(String s, int i, List<List<String>> result, List<String> ans, boolean[][] f) {
        if (i == s.length()) {
            result.add(new ArrayList<>(ans));
            return;
        }

        for (int j = i;j < s.length();j++) {
            if (f[i][j]) {
                ans.add(s.substring(i, j + 1));
                dfs(s, j + 1, result, ans, f);
                ans.remove(ans.size() - 1);
            }
        }
    }


    public static void main(String[] args) {
        System.out.println(new _131PalindromePartitioning().partition("aab"));
    }
}
