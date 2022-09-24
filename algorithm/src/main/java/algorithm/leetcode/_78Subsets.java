package algorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaobo
 * @Description
 * @date 2022/9/24 18:52
 */
public class _78Subsets {

    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(result, new ArrayList<>(), nums, 0);
        return result;
    }


    private void backtrack(List<List<Integer>> result, List<Integer> list, int[] nums, int start) {
        result.add(new ArrayList<>(list));

        for (int i = start; i < nums.length; i++) {
            list.add(nums[i]);

            backtrack(result, list, nums, i + 1);

            list.remove(list.size() - 1);
        }
    }
}
