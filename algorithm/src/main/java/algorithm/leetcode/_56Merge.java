package algorithm.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author zhaobo
 * @Description
 * @date 2022/9/25 19:25
 */
public class _56Merge {
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(o -> o[0]));

        List<int[]> result = new ArrayList<>();

        result.add(intervals[0]);
        for (int i = 1; i < intervals.length; i++) {
            int[] curr = intervals[i];
            int[] lastInterval = result.get(result.size() - 1);
            if (lastInterval[1] >= curr[0]) {
                if (lastInterval[1] < curr[1]) {
                    lastInterval[1] = curr[1];
                }
            } else {
                result.add(curr);
            }
        }
        return result.toArray(new int[][]{});
    }

    public static void main(String[] args) {
        int[][] merge = new _56Merge().merge(new int[][]{{1, 4}, {2, 3}});
        for (int[] ints : merge) {
            System.out.println(Arrays.toString(ints));
        }
    }

}
