package algorithm.leetcode;

import java.util.Arrays;

/**
 * @author zhaobo
 * @Description
 * @date 2022/9/25 0:17
 */
public class _34SearchRange {
    public int[] searchRange(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return new int[]{-1, -1};
        }
        int index = binarySearch(nums, target, 0, nums.length - 1);

        if (index == -1) {
            return new int[]{-1, -1};
        }

        int start = index - 1;
        while (start >= 0) {
            if (nums[start] == target) {
                start--;
            } else {
                break;
            }
        }
        int end = index + 1;
        while (end <= nums.length - 1) {
            if (nums[end] == target) {
                end++;
            } else {
                break;
            }
        }
        return new int[]{start + 1, end - 1};
    }

    private int binarySearch(int[] nums, int target, int start, int end) {
        if (start > end) {
            return -1;
        }
        int mid = start + (end - start) / 2;
        if (target > nums[mid]) {
            return binarySearch(nums, target, mid + 1, end);
        } else if (target < nums[mid]) {
            return binarySearch(nums, target, start, mid - 1);
        } else {
            return mid;
        }
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(new _34SearchRange().searchRange(new int[]{5, 7, 7, 8, 8, 10}, 8)));
    }
}
