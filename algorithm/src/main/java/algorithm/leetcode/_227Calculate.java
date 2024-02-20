package algorithm.leetcode;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author zhaobo
 * @Description
 * @date 2023/2/12 13:38
 */
public class _227Calculate {

    public int calculate(String s) {
        s = s.replace(" ", "");
        Deque<Long> stack = new LinkedList<>();
        Deque<Character> calculator = new LinkedList<>();

        int i = 0;
        StringBuilder sb = new StringBuilder();
        while (i < s.length()) {
            char charAt = s.charAt(i);
            if (charAt >= '0' && charAt <= '9') {
                sb.append(charAt);
            }

            if (i == s.length() - 1 || !(charAt >= '0' && charAt <= '9')){
                long num = Long.parseLong(sb.toString());
                stack.push(num);
                sb = new StringBuilder();

                Character lastCalculator = calculator.peek();

                if (lastCalculator != null && (lastCalculator == '*' || lastCalculator == '/')) {
                    calculator.pop();
                    Long num2 = stack.pop();
                    Long num1 = stack.pop();
                    long pushNum;
                    if (lastCalculator == '*') {
                        pushNum = num1 * num2;
                    } else {
                        pushNum = num1 / num2;
                    }
                    stack.push(pushNum);
                }

                if (!(charAt >= '0' && charAt <= '9')) {
                    calculator.push(charAt);
                }
            }
            i++;
        }


        while (!calculator.isEmpty()) {
            Character pop = calculator.pollLast();
            long num1 = stack.pollLast();
            long num2 = stack.pollLast();
            long pushNum;
            if (pop == '+') {
                pushNum = num1 + num2;
            } else {
                pushNum = num1 - num2;
            }
            stack.addLast(pushNum);
        }

        return stack.pop().intValue();
    }


    public static void main(String[] args) {
        System.out.println(new _227Calculate().calculate("0-2147483647"));
        System.out.println(new _227Calculate().calculate("322+2*2"));
        System.out.println(new _227Calculate().calculate("3*2*2"));
        System.out.println(new _227Calculate().calculate("3/2*2"));
        System.out.println(new _227Calculate().calculate("3-2*2"));
        System.out.println(new _227Calculate().calculate("1-1+1"));
        System.out.println(new _227Calculate().calculate("1+1-1"));
    }
}
