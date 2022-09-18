package algorithm.leetcode;

import java.util.Arrays;

/**
 * @author zhaobo
 * @Description
 * @date 2022/9/18 14:41
 */
public class _1624MaxLengthBetweenEqualCharacters {

    public static int maxLengthBetweenEqualCharacters(String s) {
        int[] arr = new int[26];
        Arrays.fill(arr, -1);

        int max = -1;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int charIndex = c - 'a';

            int preIndex = arr[charIndex];
            if (preIndex != -1) {
                max = Math.max(max, i - preIndex - 1);
            } else {
                arr[charIndex] = i;
            }
        }

        return max;
    }

    public static void main(String[] args) {
        System.out.println(maxLengthBetweenEqualCharacters("mgntdygtxrvxjnwksqhxuxtrv"));
    }

}
