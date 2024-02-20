package algorithm.leetcode;

import java.util.Objects;

/**
 * @author zhaobo
 * @Description
 * @date 2023/11/7 23:28
 */
public class _42TrappingRainWater {

    public int trap(int[] height) {
        if (Objects.isNull(height) || height.length <= 2) {
            return 0;
        }
        int total = 0;
        for (int i = 1; i < height.length - 1; i++) {
            int leftMax = max(height, 0, i - 1);
            int rightMax = max(height, i + 1, height.length - 1);
            int min = Math.min(leftMax, rightMax);
            if (height[i] < min) {
                total += min - height[i];
            }
        }
        return total;
    }

    private int max(int[] height, int start, int end) {
        int max = 0;
        for (int i = start; i <= end; i++) {
            max = Math.max(max, height[i]);
        }
        return max;
    }


    public static void main(String[] args) {
        System.out.println(new _42TrappingRainWater().trap(new int[] {4,2,0,3,2,5}));
    }
}
