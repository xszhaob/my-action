package algorithm.leetcode;

import java.util.Arrays;

/**
 * @author zhaobo
 * @Description
 * @date 2023/2/12 16:29
 */
public class _239MaxSlidingWindow {

    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums.length < k) {
            return null;
        }

        int resultLength = nums.length - k + 1;
        int[] result = new int[resultLength];

        int[] maxAndIndex = new int[2];
        maxAndIndex[0] = -1;

        for (int i = 0; i < resultLength; i++) {
            result[i] = findMaxAndIndex(nums, i, i + k, maxAndIndex);
        }

        return result;
    }

    private int findMaxAndIndex(int[] nums, int start, int end, int[] maxAndIndex) {
        if (maxAndIndex[0] >= start && maxAndIndex[0] < end) {
            int num = nums[end - 1];
            if (num >= maxAndIndex[1]) {
                maxAndIndex[0] = end - 1;
                maxAndIndex[1] = num;
            }
            return maxAndIndex[1];
        }


        maxAndIndex[0] = start;
        maxAndIndex[1] = nums[start];
        start++;
        while (start < end) {
            if (nums[start] >= maxAndIndex[1]) {
                maxAndIndex[0] = start;
                maxAndIndex[1] = nums[start];
            }
            start++;
        }
        return maxAndIndex[1];
    }


    public static void main(String[] args) {
        System.out.println(Arrays.toString(new _239MaxSlidingWindow().maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3)));
    }
}
