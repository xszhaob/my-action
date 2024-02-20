package algorithm.leetcode;

import java.util.Arrays;

/**
 * @author zhaobo
 * @Description
 * @date 2023/2/5 23:17
 */
public class _289GameOfLife {

    public void gameOfLife(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = calculate(board, i, j);
            }
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 3) {
                    // 3表示原来是1，现在变成0
                    board[i][j] = 0;
                }
                if (board[i][j] == 2) {
                    // 2表示原来是0，变成1
                    board[i][j] = 1;
                }
            }
        }
    }

    private int calculate(int[][] board, int i, int j) {
        int m = board.length - 1;
        int n = board[0].length - 1;
        int live = 0;
        if (i - 1 >= 0 && j - 1 >= 0) {
            if (board[i - 1][j - 1] == 1 || board[i - 1][j - 1] == 3) {
                live++;
            }
        }
        if (i - 1 >= 0) {
            if (board[i - 1][j] == 1 || board[i - 1][j] == 3) {
                live++;
            }
        }
        if (i - 1 >= 0 && j + 1 <= n) {
            if (board[i - 1][j + 1] == 1 || board[i - 1][j + 1] == 3) {
                live++;
            }
        }
        if (j + 1 <= n) {
            if (board[i][j + 1] == 1 || board[i][j + 1] == 3) {
                live++;
            }
        }
        if (i + 1 <= m && j + 1 <= n) {
            if (board[i + 1][j + 1] == 1 || board[i + 1][j + 1] == 3) {
                live++;
            }
        }
        if (i + 1 <= m) {
            if (board[i + 1][j] == 1 || board[i + 1][j] == 3) {
                live++;
            }
        }
        if (i + 1 <= m && j - 1 >= 0) {
            if (board[i + 1][j - 1] == 1 || board[i + 1][j - 1] == 3) {
                live++;
            }
        }
        if (j - 1 >= 0) {
            if (board[i][j - 1] == 1 || board[i][j - 1] == 3) {
                live++;
            }
        }

        if (board[i][j] == 1) {
            // 3表示原来是1，现在变成0
            return live < 2 || live > 3 ? 3 : 1;
        } else {
            // 2表示原来是0，变成1
            return live == 3 ? 2 : 0;
        }
    }


    public static void main(String[] args) {
        int[][] life = new int[][]{{0, 1, 0}, {0, 0, 1}, {1, 1, 1}, {0, 0, 0}};
        new _289GameOfLife().gameOfLife(life);
        for (int[] ints : life) {
            System.out.println(Arrays.toString(ints));
        }

        int[][] life2 = new int[][]{{1, 1}, {1, 0}};
        new _289GameOfLife().gameOfLife(life2);
        for (int[] ints : life2) {
            System.out.println(Arrays.toString(ints));
        }
    }
}
