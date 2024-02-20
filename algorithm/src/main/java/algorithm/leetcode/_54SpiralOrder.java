package algorithm.leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zhaobo
 * @Description
 * @date 2023/2/5 16:44
 */
public class _54SpiralOrder {
    public List<Integer> spiralOrder(int[][] matrix) {
        if (Objects.isNull(matrix) || matrix.length == 0 || (matrix[0] == null || matrix[0].length == 0)) {
            return new ArrayList<>(0);
        }

        // 方向1右、2下、3左、4上
        int direction = 1;

        int i = 0, j = 0;
        int rightToIndex = matrix[0].length - 1;
        int downToIndex = matrix.length - 1;
        int leftToIndex = 0;
        int upToIndex = 1;

        int totalEle = matrix.length * matrix[0].length;
        List<Integer> result = new ArrayList<>(totalEle);
        while (true) {
            result.add(matrix[i][j]);
            if (result.size() == totalEle) {
                return result;
            }
            if (direction == 1) { // 向右
                if (j == rightToIndex) {
                    direction = 2;
                    rightToIndex--;
                    i++;
                } else {
                    j++;
                }
            } else if (direction == 2) { // 向下
                if (i == downToIndex) {
                    direction = 3;
                    downToIndex--;
                    j--;
                } else {
                    i++;
                }
            } else if (direction == 3) { // 向左
                if (j == leftToIndex) {
                    direction = 4;
                    leftToIndex++;
                    i--;
                } else {
                    j--;
                }
            } else {// 向上
                if (i == upToIndex) {
                    direction = 1;
                    upToIndex++;
                    j++;
                } else {
                    i--;
                }
            }
        }
    }


    public static void main(String[] args) {
        System.out.println(new _54SpiralOrder().spiralOrder(new int[][]{{1,2,3,4},{5,6,7,8},{9,10,11,12}}));
    }
}
