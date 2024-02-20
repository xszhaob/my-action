package algorithm.leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author zhaobo
 * @date 2023/11/25 23:09
 */
public class _301RemoveInvalidParentheses {

    /**
     * 整体思路：
     * 1）先得到需要删除的最少括号的数量；
     * 2）使用回溯法，尝试所有可能的删除非法括号的方案。
     * <p>
     * 针对效率问题：
     * 1）如果需要删除的左括号和右括号数量都为0时，直接判断删除了非法括号的字符串是否有效括号；
     * 2）回溯过程中，如果剩余的字符串长度小于需要删除的括号数量时，直接认定该方案不可行；
     * <p>
     * 针对方案重复问题：
     * 如果遇到连续的n个字符是相同的括号，则只对这n个连续的括号回溯一次。
     */
    public List<String> removeInvalidParentheses(String s) {
        if (Objects.isNull(s) || s.trim().isEmpty()) {
            return new ArrayList<>(0);
        }

        int lr = 0;
        int rr = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                lr++;
            } else if (s.charAt(i) == ')') {
                if (lr > 0) {
                    lr--;
                } else {
                    rr++;
                }
            }
        }

        List<String> result = new ArrayList<>();
        extract(s, 0, lr, rr, result);

        return result;
    }

    private void extract(String s, int start, int lr, int rr, List<String> result) {
        if (lr == 0 && rr == 0) {
            if (isValid(s)) {
                result.add(s);
            }
            return;
        }

        for (int i = start; i < s.length(); i++) {
            if (i > start && s.charAt(i) == s.charAt(i - 1)) {
                continue;
            }

            if (lr + rr > s.length() - i) {
                return;
            }

            String newStr = s.substring(0, i) + s.substring(i + 1);
            if (lr > 0 && s.charAt(i) == '(') {
                extract(newStr, i, lr - 1, rr, result);
            }

            if (rr > 0 && s.charAt(i) == ')') {
                extract(newStr, i, lr, rr - 1, result);
            }
        }
    }

    /**
     * 字符串是有效括号？
     */
    private boolean isValid(String s) {
        int cnt = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                cnt++;
            } else if (s.charAt(i) == ')') {
                cnt--;
                if (cnt < 0) {
                    return false;
                }
            }
        }
        return cnt == 0;
    }


    public static void main(String[] args) {
        List<String> strings = new _301RemoveInvalidParentheses().removeInvalidParentheses(")(");
        System.out.println(strings);
    }
}
