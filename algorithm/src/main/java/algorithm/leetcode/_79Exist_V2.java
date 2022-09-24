package algorithm.leetcode;

/**
 * @author zhaobo
 * @Description
 * @date 2022/9/24 19:19
 */
public class _79Exist_V2 {
    public boolean exist(char[][] board, String word) {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (backtrack(board, word, 0, i, j)) {
                    return true;
                }
            }
        }


        return false;
    }


    private boolean backtrack(char[][] board, String word, int index, int i, int j) {
        if (index == word.length()) {
            return true;
        }

        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || word.charAt(index) != board[i][j]) {
            return false;
        }

        char temp = board[i][j];
        board[i][j] = '1';

        boolean result = false;

        result |= backtrack(board, word, index + 1, i + 1, j);
        result |= backtrack(board, word, index + 1, i - 1, j);
        result |= backtrack(board, word, index + 1, i, j + 1);
        result |= backtrack(board, word, index + 1, i, j - 1);

        board[i][j] = temp;

        return result;
    }


    public static void main(String[] args) {
        System.out.println(new _79Exist_V2().exist(new char[][]{
                {'A', 'B', 'C', 'E' }, {'S', 'F', 'C', 'S' }, {'A', 'D', 'E', 'E' }
        }, "ABCESEECFDAS"));
    }
}
