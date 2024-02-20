package algorithm.leetcode;

import java.util.Arrays;

/**
 * @author zhaobo
 * @Description
 * @date 2022/9/25 20:13
 */
public class _240SearchMatrix {

    public boolean searchMatrix(int[][] matrix, int target) {
        for (int[] ints : matrix) {
            int result = Arrays.binarySearch(ints, target);
            if (result >= 0) {
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        System.out.println(new _240SearchMatrix().searchMatrix(new int[][]{
                {1, 4, 7, 11, 15}, {2, 5, 8, 12, 19}, {3, 6, 9, 16, 22}, {10, 13, 14, 17, 24}, {18, 21, 23, 26, 30}
        }, 20));
    }
}
