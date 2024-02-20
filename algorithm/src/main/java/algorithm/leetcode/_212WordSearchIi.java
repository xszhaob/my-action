package algorithm.leetcode;

import java.util.*;

/**
 * @author zhaobo
 * @Description
 * @date 2023/11/13 23:22
 */
public class _212WordSearchIi {

    private int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    public List<String> findWords(char[][] board, String[] words) {
        if (Objects.isNull(board) || board.length == 0 || board[0].length == 0) {
            return new ArrayList<>(0);
        }
        if (Objects.isNull(words) || words.length == 0) {
            return new ArrayList<>(0);
        }

        Set<String> result = new HashSet<>();
        Trie trie = new Trie();
        for (String word : words) {
            trie.insert(word);
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                dfs(board, trie, i, j, result);
            }
        }

        return new ArrayList<>(result);
    }

    private void dfs(char[][] board, Trie now, int i, int j, Set<String> result) {
        if (!now.children.containsKey(board[i][j])) {
            return;
        }

        char c = board[i][j];
        Trie next = now.children.get(c);
        if (!"".equals(next.word)) {
            result.add(next.word);
        }

        board[i][j] = '#';
        for (int[] dir : dirs) {
            int i1 = i + dir[0];
            int j1 = j + dir[1];
            if (i1 >= 0 && i1 < board.length && j1 >= 0 && j1 < board[0].length) {
                dfs(board, next, i1, j1, result);
            }
        }
        board[i][j] = c;
    }


    public static class Trie {
        String word;
        Map<Character, Trie> children;

        public Trie() {
            this.word = "";
            this.children = new HashMap<>();
        }

        public void insert(String word) {
            Trie cur = this;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (!cur.children.containsKey(c)) {
                    cur.children.put(c, new Trie());
                }
                cur = cur.children.get(c);
            }
            cur.word = word;
        }
    }

    public static void main(String[] args) {
        char[][] board = new char[][]{{'o', 'a', 'a', 'n'}, {'e', 't', 'a', 'e'}, {'i', 'h', 'k', 'r'}, {'i', 'f', 'l', 'v'}};
        String[] words = new String[]{"oath", "pea", "eat", "rain"};
        System.out.println(new _212WordSearchIi().findWords(board, words));
    }
}
