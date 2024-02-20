package algorithm.leetcode;

import java.util.Objects;

/**
 * @author zhaobo
 * @Description
 * @date 2023/2/5 22:41
 * <p>
 * 数组双指针
 */
public class _11MaxArea {
    public int maxArea(int[] height) {
        if (Objects.isNull(height) || height.length < 2) {
            return 0;
        }

        // 实质就是在移动的过程中不断消去不可能成为最大值的状态
        int left = 0;
        int right = height.length - 1;
        int max = 0;
        while (left < right) {
            int min = Math.min(height[left], height[right]);
            max = Math.max(max, (right - left) * min);
            if (height[left] > height[right]) {
                right--;
            } else {
                left++;
            }
        }
        return max;
    }
}
