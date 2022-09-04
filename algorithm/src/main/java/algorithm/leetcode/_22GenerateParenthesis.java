package algorithm.leetcode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zhaobo
 * @Description
 * @date 2022/9/4 15:00
 */
public class _22GenerateParenthesis {

    public static void main(String[] args) {

    }

    /**
     * 使用动态规划解答
     */
    public static List<String> generateParenthesis(int n) {
        if (n == 0) {
            return new ArrayList<>(0);
        }
        LinkedList<LinkedList<String>> result = new LinkedList<>();
        LinkedList<String> list0 = new LinkedList<>();
        list0.add("");
        result.add(list0);

        LinkedList<String> list1 = new LinkedList<>();
        list1.add("()");
        result.add(list1);

        for (int i = 2; i <= n; i++) {
            LinkedList<String> temp = new LinkedList<String>();
            for (int j = 0; j < i; j++) {
                List<String> str1 = result.get(j);
                List<String> str2 = result.get(i - 1 - j);
                for (String s1 : str1) {
                    for (String s2 : str2) {
                        String el = "(" + s1 + ")" + s2;
                        temp.add(el);
                    }
                }

            }
            result.add(temp);
        }

        return result.get(n);
    }


    public static List<String> generateParenthesis1(int n) {
        if (n == 0) {
            return new ArrayList<>(0);
        }
        LinkedList<String> result = new LinkedList<>();
        getParenthesis(result, new StringBuilder (), n, n);
        return result;
    }

    public static void getParenthesis(List<String> result, StringBuilder sb, int left, int right) {
        if (left == 0 && right == 0) {
            result.add(sb.toString());
            return;
        }

        if (left == right) {
            getParenthesis(result, sb.append("("), left - 1, right);
        } else if (left < right) {
            if (left > 0) {
                getParenthesis(result, sb.append("("), left - 1, right);
            }
            getParenthesis(result, sb.append(")"), left, right - 1);
        }
    }
}
