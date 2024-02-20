package algorithm.leetcode;

/**
 * @author zhaobo
 * @Description
 * @date 2022/9/25 20:21
 */
public class _55CanJump {

    public boolean canJump(int[] nums) {
        int n = nums.length;
        int last = n - 1;

        for (int i = nums.length - 2; i >= 0; i--) {
            if (i + nums[i] >= last) {
                last = i;
            }
        }

        return last == 0;

    }
}
