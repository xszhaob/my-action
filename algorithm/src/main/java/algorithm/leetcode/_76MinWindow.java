package algorithm.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaobo
 * @Description
 * @date 2023/2/12 17:22
 */
public class _76MinWindow {
    public String minWindow(String s, String t) {
        Map<Character, Integer> map = new HashMap<>();
        Map<Character, Integer> windows = new HashMap<>();
        for (int i = 0; i < t.length(); i++) {
            Integer integer = map.get(t.charAt(i));
            if (integer == null) {
                integer = 0;
            }
            integer++;
            map.put(t.charAt(i), integer);
            windows.put(t.charAt(i), 0);
        }

        int windowsStart = 0;
        int valid = 0;

        int right = 0;
        int left = 0;
        int length = Integer.MAX_VALUE;
        while (right < s.length()) {
            Character charAt = s.charAt(right++);
            if (map.containsKey(charAt)) {
                Integer integer = windows.get(charAt);
                integer++;
                windows.put(charAt, integer);

                if (integer.equals(map.get(charAt))) {
                    valid++;
                }
            }

            while (valid == map.size()) {
                if (right - left < length) {
                    windowsStart = left;
                    length = right - left;
                }
                Character c = s.charAt(left++);
                if (map.containsKey(c)) {
                    Integer integer = windows.get(c);
                    integer--;
                    windows.put(c, integer);
                    if (map.get(c) > integer) {
                        valid--;
                    }
                }

            }
        }
        return length == Integer.MAX_VALUE ? "" : s.substring(windowsStart, windowsStart + length);
    }


    public static void main(String[] args) {
        System.out.println(new _76MinWindow().minWindow("ADOBECODEBANC", "ABC"));
        System.out.println(new _76MinWindow().minWindow("a", "a"));
        System.out.println(new _76MinWindow().minWindow("a", "aa"));
        System.out.println(new _76MinWindow().minWindow("aa", "aa"));
    }
}
