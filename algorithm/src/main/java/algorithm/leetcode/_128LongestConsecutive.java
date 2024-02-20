package algorithm.leetcode;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zhaobo
 * @Description
 * @date 2023/2/12 11:47
 */
public class _128LongestConsecutive {

    public int longestConsecutive(int[] nums) {
        Set<Integer> sets = new HashSet<>();
        for (int num : nums) {
            sets.add(num);
        }

        int result = 0;
        for (int num : nums) {
            // 只有是连续数字中的第一个时，才会进入后面的逻辑
            if (sets.contains(num - 1)) {
                continue;
            }
            int start = 0;
            int end = 0;
            int n = num;
            while (sets.contains(n + 1)) {
                n++;
                end++;
            }
            result = Math.max(end - start + 1, result);
            if (result == nums.length) {
                break;
            }
        }

        return result;
    }


    public static void main(String[] args) {
        System.out.println(new _128LongestConsecutive().longestConsecutive(new int[]{100,4,200,1,3,2}));
        System.out.println(new _128LongestConsecutive().longestConsecutive(new int[]{0,3,7,2,5,8,4,6,0,1}));
    }

}
