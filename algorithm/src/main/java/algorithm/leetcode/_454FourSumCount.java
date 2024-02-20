package algorithm.leetcode;

import java.util.*;

/**
 * @author zhaobo
 * @Description
 * @date 2023/2/5 17:53
 */
public class _454FourSumCount {

    public int fourSumCount(int[] nums1, int[] nums2, int[] nums3, int[] nums4) {
        if (Objects.isNull(nums1) || nums1.length == 0
                || Objects.isNull(nums2) || nums2.length == 0
                || Objects.isNull(nums3) || nums3.length == 0
                || Objects.isNull(nums4) || nums4.length == 0) {
            return 0;
        }

        List<int[]> numsList = new ArrayList<>(4);
        numsList.add(nums1);
        numsList.add(nums2);
        numsList.add(nums3);
        numsList.add(nums4);

        Map<Integer, Map<Integer, Integer>> cache = new HashMap<>();
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);
        cache.put(numsList.size(), map);

        fourSumCount(numsList, cache, 0);

        Integer result = cache.get(0).get(0);
        return result == null ? 0 : result;

    }


    public void fourSumCount(List<int[]> numsList, Map<Integer, Map<Integer, Integer>> cache, int level) {
        if (level == numsList.size()) {
            return;
        }

        Map<Integer, Integer> map = cache.get(level + 1);
        if (Objects.isNull(map)) {
            fourSumCount(numsList, cache, level + 1);
        }
        map = cache.get(level + 1);

        Map<Integer, Integer> levelMap = new HashMap<>();
        int[] nums = numsList.get(level);
        for (int num : nums) {
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                Integer integer = levelMap.get(num + entry.getKey());
                if (Objects.isNull(integer)) {
                    integer = entry.getValue();
                } else {
                    integer = integer + entry.getValue();
                }
                levelMap.put(num + entry.getKey(), integer);
            }
        }
        cache.put(level, levelMap);
    }


    public int fourSumCount_1(int[] nums1, int[] nums2, int[] nums3, int[] nums4) {
        Map<Integer, Integer> map1 = new HashMap<>();
        for (int i : nums1) {
            for (int j : nums2) {
                Integer integer = map1.get(i + j);
                if (integer == null) {
                    integer = 1;
                } else {
                    integer++;
                }
                map1.put(i + j, integer);
            }
        }


        int result = 0;
        for (int i : nums3) {
            for (int j : nums4) {
                Integer integer = map1.get(-(i + j));
                if (integer != null) {
                    result += integer;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(new _454FourSumCount().fourSumCount(new int[]{1, 2}, new int[]{-2, -1}, new int[]{-1, 2}, new int[]{0, 2}));
        System.out.println(new _454FourSumCount().fourSumCount_1(new int[]{1, 2}, new int[]{-2, -1}, new int[]{-1, 2}, new int[]{0, 2}));
        System.out.println(new _454FourSumCount().fourSumCount(new int[]{0}, new int[]{0}, new int[]{0}, new int[]{0}));
        System.out.println(new _454FourSumCount().fourSumCount_1(new int[]{0}, new int[]{0}, new int[]{0}, new int[]{0}));
        int[] a = new int[]{-30, -4, -4, -24, -31, 9, -8, 8, -30, -5, -17, 5, -13, -2, -18, -25, 7, -12, 2, -21, -28, -20, 2, -5, -18, -14, -10, -15, -11, -28, 7, 8, -23, -31, -7, -11, 5, 4, -24, -25, -4, -32, -23, -19, 0, -31, -4, 5, -7, 7, 7, 2, 10, -32, 5, -7, -31, 9, -21, -6, 2, -9, 7, 7, -14, -4, -14, -15, -24, -26, 5, -30, 8, -31, -18, -16, -31, 0, -10, -15, -14, -24, 7, 2, -27, -25, -3, 3, -30, 9, 2, -19, -5, -22, -23, -31, -18, 1, -16, 0};
        int[] b = new int[]{-22, 4, 2, -18, -20, -23, 4, 6, -1, -10, -25, -19, -3, 6, -21, 2, -4, -11, 6, -2, 9, -18, -3, -7, -11, -12, -5, -18, 0, 5, 9, -8, 1, -12, -28, -1, 5, -27, -16, -2, -19, 8, -14, -24, -27, 6, -21, 8, -14, -8, -5, -25, 0, -30, -24, -15, -11, -21, -11, -7, -22, -8, -7, -3, -26, -8, -4, -17, -22, -9, -14, -16, 9, -32, -24, -9, 9, -10, -21, -8, -4, -23, 6, 7, -9, -28, -26, -18, -12, 0, -2, -24, 3, -10, -11, -18, -3, -29, -17, -7};
        int[] c = new int[]{7, -31, 1, -19, -28, -30, -10, -18, -4, -10, -1, -28, -6, -21, 5, 8, 8, -20, -13, -14, -28, -32, 4, -26, -7, -31, 2, -5, -32, -2, -3, 3, 7, -4, 8, 9, 5, 1, -22, -30, -27, -8, -14, 0, -9, -2, 10, -3, -20, -7, 4, 5, 5, -16, -1, 10, -16, -9, -19, 6, -13, -19, -27, -5, -8, 9, -2, -26, -29, 3, -25, -28, -23, -6, -8, -14, -20, -30, 5, -32, -24, -2, -15, -26, -26, 2, -17, -10, -26, 4, -6, -31, -20, -8, -24, -28, -26, -12, 2, 0};
        int[] d = new int[]{8, -19, 9, -12, -1, -11, -17, 6, -23, 6, 3, -29, -17, 9, -1, 1, -2, 6, 7, -28, 6, -28, -24, -4, -21, -8, -8, -14, -19, -10, 8, -12, 5, -5, 9, -13, -19, -8, -1, -5, -4, -29, 0, -10, 5, -11, -6, -2, 1, -11, 7, -3, -1, 5, -18, -24, -22, -20, 6, -28, -5, -4, 6, -7, -1, -6, 9, -28, -13, -16, -2, -29, -19, 2, -21, -20, -1, -3, -24, -15, 2, -2, 6, -15, -19, 9, 6, -2, 2, -26, 0, -26, -14, -27, -20, -29, -31, -23, 6, 0};
        System.out.println(new _454FourSumCount().fourSumCount(a, b, c, d));
        System.out.println(new _454FourSumCount().fourSumCount_1(a, b, c, d));
        System.out.println(new _454FourSumCount().fourSumCount(new int[]{-1, -1}, new int[]{-1, 1}, new int[]{-1, 1}, new int[]{1, -1}));
        System.out.println(new _454FourSumCount().fourSumCount_1(new int[]{-1, -1}, new int[]{-1, 1}, new int[]{-1, 1}, new int[]{1, -1}));
    }
}
