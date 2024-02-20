package algorithm.leetcode;

import java.util.Objects;

/**
 * @author zhaobo
 * @Description
 * @date 2023/11/7 23:28
 */
public class _42TrappingRainWater_2 {

    public int trap(int[] height) {
        if (Objects.isNull(height) || height.length <= 2) {
            return 0;
        }
        int[] rightMaxArr = new int[height.length];
        rightMaxArr[height.length - 1] = height[height.length - 1];
        for (int i = height.length - 2; i >= 0; i--) {
            rightMaxArr[i] = Math.max(height[i], rightMaxArr[i + 1]);
        }

        int total = 0;


        int leftMax = 0;

        for (int i = 1; i < height.length - 1; i++) {
            leftMax = Math.max(leftMax, height[i - 1]);
            int rightMax = rightMaxArr[i + 1];
            int min = Math.min(leftMax, rightMax);
            if (height[i] < min) {
                total += min - height[i];
            }
        }
        return total;
    }


    public static void main(String[] args) {
        System.out.println(new _42TrappingRainWater_2().trap(new int[]{0,1,0,2,1,0,1,3,2,1,2,1}));
    }
}
