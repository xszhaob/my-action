package algorithm.leetcode;

import java.util.Arrays;

/**
 * @author zhaobo
 * @Description
 * @date 2022/9/24 21:13
 */
public class _75SortColors {

    public void sortColors(int[] nums) {
        if (nums == null) {
            return;
        }
        int redNext = 0;
        int bluePre = nums.length - 1;
        int i = 0;
        while (i <= bluePre) {
            // 红色
            if (nums[i] == 0) {
                if (i == redNext) {
                    i++;
                } else {
                    swap(nums, redNext, i);
                }
                redNext++;
            } else if (nums[i] == 2) { // 蓝色
                swap(nums, bluePre, i);
                bluePre--;
            } else {
                i++;
            }
        }
    }

    private void swap(int[] nums, int redNext, int i) {
        int num = nums[redNext];
        nums[redNext] = nums[i];
        nums[i] = num;
    }


    public static void main(String[] args) {
        int[] ints = {2, 0, 1};
        new _75SortColors().sortColors(ints);
        System.out.println(Arrays.toString(ints));
    }
}
