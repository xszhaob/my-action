package algorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaobo
 * @Description
 * @date 2023/2/12 20:11
 */
public class sortList {

    public ListNode sortList(ListNode head) {
        if (head == null) {
            return null;
        }
        List<Integer> list = new ArrayList<>();
        while (head != null) {
            list.add(head.val);
            head = head.next;
        }

        list.sort(Integer::compare);

        ListNode result = null;
        ListNode pre = null;
        for (Integer integer : list) {
            ListNode current = new ListNode(integer);
            if (pre == null) {
                pre = current;
            } else {
                pre.next = current;
                pre = pre.next;
            }
            if (head == null) {
                head = current;
            }
        }
        return head;
    }


    private static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }


    public static void main(String[] args) {
        printNode(new sortList().sortList(new ListNode(4, new ListNode(2, new ListNode(1, new ListNode(3))))));
        printNode(new sortList().sortList(new ListNode(-1, new ListNode(5, new ListNode(3, new ListNode(4, new ListNode(0)))))));
    }


    private static void printNode(ListNode node) {
        List<Integer> list = new ArrayList<>();
        while (node != null) {
            list.add(node.val);
            node = node.next;
        }
        System.out.println(list);
    }
}
