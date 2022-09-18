package algorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaobo
 * @Description
 * @date 2022/9/18 17:41
 */
public class _46Permute {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();

        List<Integer> numList = new ArrayList<>();
        for (int num : nums) {
            numList.add(num);
        }
        backtrack(numList, result, new ArrayList<>());
        return result;
    }


    public void backtrack(List<Integer> nums, List<List<Integer>> result, List<Integer> list) {
        if (list.size() == nums.size()) {
            result.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < nums.size(); i++) {
            if (nums.get(i) == -11) {
                continue;
            }
            Integer num = nums.get(i);
            list.add(num);
            nums.set(i, -11);
            backtrack(nums, result, list);
            nums.set(i, num);
            list.remove(list.size() - 1);
        }
    }


    public static void main(String[] args) {
        System.out.println(new _46Permute().permute(new int[]{1, 2, 3}));
        System.out.println(new _46Permute().permute(new int[]{0, 1}));
        System.out.println(new _46Permute().permute(new int[]{1}));
    }
}
