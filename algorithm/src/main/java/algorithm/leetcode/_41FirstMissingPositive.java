package algorithm.leetcode;

/**
 * @author zhaobo
 * @Description
 * @date 2023/2/12 11:27
 */
public class _41FirstMissingPositive {
    public int firstMissingPositive(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            while (nums[i] <= nums.length && nums[i] > 0 && nums[nums[i] - 1] != nums[i]) {
                int num = nums[i];
                int n = nums[num - 1];
                nums[i] = n;
                nums[num - 1] = num;
            }
        }


        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }
        return nums.length + 1;
    }


    public static void main(String[] args) {
        System.out.println(new _41FirstMissingPositive().firstMissingPositive(new int[] {1,2,0}));
        System.out.println(new _41FirstMissingPositive().firstMissingPositive(new int[] {3,4,-1,1}));
        System.out.println(new _41FirstMissingPositive().firstMissingPositive(new int[] {7,8,9,11,12}));
    }
}
