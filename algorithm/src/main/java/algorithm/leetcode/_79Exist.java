package algorithm.leetcode;

import java.util.Arrays;

/**
 * @author zhaobo
 * @Description
 * @date 2022/9/24 19:19
 */
public class _79Exist {
    public boolean exist(char[][] board, String word) {
        boolean[][] use = new boolean[board.length][board[0].length];
        int[][] exist = new int[word.length()][2];
        for (int[] ints : exist) {
            Arrays.fill(ints, -1);
        }


        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == word.charAt(0)) {
                    backtrack(use, exist, board, word, 0, i, j);
                    if (exist[word.length() - 1][0] != -1) {
                        return true;
                    }
                }
            }
        }


        return false;
    }


    private void backtrack(boolean[][] use, int[][] exist, char[][] board, String word, int index, int i, int j) {
        if (index == word.length()) {
            return;
        }

        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) {
            return;
        }

        if (use[i][j]) {
            return;
        }

        if (word.charAt(index) != board[i][j]) {
            return;
        }

        use[i][j] = true;
        exist[index] = new int[]{i, j};

        backtrack(use, exist, board, word, index + 1, i + 1, j);
        backtrack(use, exist, board, word, index + 1, i - 1, j);
        backtrack(use, exist, board, word, index + 1, i, j + 1);
        backtrack(use, exist, board, word, index + 1, i, j - 1);

        if (index + 1 < word.length() && exist[index + 1][0] == -1) {
            use[i][j] = false;
            exist[index] = new int[] {-1, -1};
        }
    }


    public static void main(String[] args) {
        System.out.println(new _79Exist().exist(new char[][] {
                {'A','B','C','E'},{'S','F','C','S'},{'A','D','E','E'}
        }, "ABCESEECFDAD"));
    }
}
