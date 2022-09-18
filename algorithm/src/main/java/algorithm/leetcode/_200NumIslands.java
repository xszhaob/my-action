package algorithm.leetcode;

/**
 * @author zhaobo
 * @Description
 * @date 2022/9/18 16:45
 */
public class _200NumIslands {
    public int numIslands(char[][] grid) {
        // 边界条件
        if (grid == null || grid.length == 0) {
            return 0;
        }
        // 统计岛屿的个数
        int num = 0;
        // 遍历二维数组
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                // 遇到为1的格子
                if (grid[i][j] == '1') {
                    // 岛屿数+1
                    num++;
                    // 利用深度优先算法，清理该格子附近所有为1的格子
                    clearUpIslands(grid, i, j);
                }
            }
        }
        return num;
    }

    private void clearUpIslands(char[][] grid, int i, int j) {
        // 终止条件：下标越界
        if (i >= grid.length || i < 0 || j >= grid[0].length || j < 0) {
            return;
        }
        // 终止条件：格子为0
        if (grid[i][j] == '0') {
            return;
        }
        // 格子为1的情况
        if (grid[i][j] == '1') {
            // 置为0
            grid[i][j] = '0';
            // 格子下方
            clearUpIslands(grid, i + 1, j);
            // 格子上方
            clearUpIslands(grid, i - 1, j);
            // 格子右方
            clearUpIslands(grid, i, j + 1);
            // 格子左方
            clearUpIslands(grid, i, j - 1);
        }
    }


    public static void main(String[] args) {
        System.out.println(new _200NumIslands().numIslands(new char[][]{
                {'1', '1', '0', '0', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '1', '0', '0'},
                {'0', '0', '0', '1', '1'}
        }));
    }
}
