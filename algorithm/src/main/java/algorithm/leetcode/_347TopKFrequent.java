package algorithm.leetcode;

import java.util.*;

/**
 * @author zhaobo
 * @Description
 * @date 2022/9/24 23:00
 */
public class _347TopKFrequent {
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> numCountMap = new HashMap<>();
        for (int num : nums) {
            Integer count = numCountMap.getOrDefault(num, 0);
            numCountMap.put(num, ++count);
        }


        List<int[]> numCountList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : numCountMap.entrySet()) {
            numCountList.add(new int[]{entry.getValue(), entry.getKey()});
        }
        numCountList.sort((o1, o2) -> Integer.compare(o2[0], o1[0]));

        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = numCountList.get(i)[1];
        }
        return result;
    }


    public static void main(String[] args) {
        System.out.println(Arrays.toString(new _347TopKFrequent().topKFrequent(new int[]{1}, 1)));
    }
}
