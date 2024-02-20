package algorithm.leetcode;

import java.util.Arrays;

/**
 * @author zhaobo
 * @Description
 * @date 2023/2/5 16:05
 */
public class _238ProductOfArrayExceptSelf {

    /**
     * 使用两个数组
     * O(n)空间复杂度
     */
    public int[] productExceptSelf_1(int[] nums) {
        int[] left = new int[nums.length - 1];
        int[] right = new int[nums.length];

        for (int i = 0; i < nums.length - 1; i++) {
            if (i == 0) {
                left[i] = nums[i];
            } else {
                left[i] = nums[i] * left[i - 1];
            }
        }

        for (int i = nums.length - 1; i > 0; i--) {
            if (i == nums.length - 1) {
                right[i] = nums[i];
            } else {
                right[i] = nums[i] * right[i + 1];
            }
        }

        int[] result = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            if (i == 0) {
                result[i] = right[i + 1];
            } else if (i == nums.length - 1) {
                result[i] = left[i - 1];
            } else {
                result[i] = left[i - 1] * right[i + 1];
            }
        }

        return result;
    }

    /**
     * 使用一个数组
     * O(1)空间复杂度。出于对空间复杂度分析的目的，输出数组不被视为额外空间
     */
    public int[] productExceptSelf_2(int[] nums) {
        int[] result = new int[nums.length];


        for (int i = 0; i < nums.length; i++) {
            if (i == 0) {
                result[i] = nums[i];
            } else {
                result[i] = nums[i] * result[i - 1];
            }
        }

        int right = 1;
        for (int i = nums.length - 1; i >= 0; i--) {
            if (i == 0) {
                result[i] = right;
            } else {
                result[i] = result[i - 1] * right;
            }
            right = nums[i] * right;
        }
        return result;
    }



    public static void main(String[] args) {
        System.out.println(Arrays.toString(new _238ProductOfArrayExceptSelf().productExceptSelf_1(new int[] {-1,1,0,-3,3})));
        System.out.println(Arrays.toString(new _238ProductOfArrayExceptSelf().productExceptSelf_2(new int[] {-1,1,0,-3,3})));
    }
}
