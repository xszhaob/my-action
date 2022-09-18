package algorithm.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhaobo
 * @Description
 * @date 2022/9/18 17:02
 */
public class _17LetterCombinations {

    public List<String> letterCombinations(String digits) {
        if (digits == null || digits.length() == 0) {
            return new ArrayList<>(0);
        }

        Map<Character, String> map = new HashMap<>(8);
        map.put('2', "abc");
        map.put('3', "def");
        map.put('4', "ghi");
        map.put('5', "jkl");
        map.put('6', "mno");
        map.put('7', "pqrs");
        map.put('8', "tuv");
        map.put('9', "wxyz");

        List<String> result = new ArrayList<>();
        backtrack(result, digits, 0, new StringBuilder(), map);

        return result;
    }

    /**
     * 回溯方法解决
     */
    private void backtrack(List<String> result, String digits, int index, StringBuilder sb, Map<Character, String> map) {
        if (index == digits.length()) {
            result.add(sb.toString());
            return;
        }

        char c = digits.charAt(index);
        String s = map.get(c);
        for (int i = 0; i < s.length(); i++) {
            char charAt = s.charAt(i);
            sb.append(charAt);
            backtrack(result, digits, index + 1, sb, map);
            sb.deleteCharAt(sb.length() - 1);
        }
    }


    public static void main(String[] args) {
        System.out.println(new _17LetterCombinations().letterCombinations("23"));
        System.out.println(new _17LetterCombinations().letterCombinations(""));
        System.out.println(new _17LetterCombinations().letterCombinations("2"));
    }
}
