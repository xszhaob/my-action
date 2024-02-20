package algorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaobo
 * @Description
 * @date 2023/2/12 19:07
 */
public class _23MergeKLists {

    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }
        return mergeKLists(lists, 0, lists.length - 1);
    }

    public ListNode mergeKLists(ListNode[] lists, int start, int end) {
        if (start == end) {
            return lists[start];
        }
        if (start > end) {
            return null;
        }

        int mid = start + (end - start) / 2;
        ListNode listNode1 = mergeKLists(lists, start, mid);
        ListNode listNode2 = mergeKLists(lists, mid + 1, end);

        return mergeList(listNode1, listNode2);
    }

    private ListNode mergeList(ListNode listNode1, ListNode listNode2) {
        if (listNode1 == null) {
            return listNode2;
        }
        if (listNode2 == null) {
            return listNode1;
        }
        ListNode head = null;
        ListNode pre = null;
        ListNode node1 = listNode1;
        ListNode node2 = listNode2;
        while (node1 != null || node2 != null) {
            ListNode newNode;
            if (node2 == null) {
                newNode = new ListNode(node1.val);
                node1 = node1.next;
            } else if (node1 == null) {
                newNode = new ListNode(node2.val);
                node2 = node2.next;
            } else {
                if (node1.val > node2.val) {
                    newNode = new ListNode(node2.val);
                    node2 = node2.next;
                } else {
                    newNode = new ListNode(node1.val);
                    node1 = node1.next;
                }
            }

            if (pre == null) {
                pre = newNode;
            } else {
                pre.next = newNode;
                pre = pre.next;
            }

            if (head == null) {
                head = newNode;
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
        test1();

        printNode(new ListNode[]{});

        printNode(new ListNode[]{null});

        test2();
    }

    private static void test1() {
        ListNode node1 = new ListNode(1, new ListNode(4, new ListNode(5)));
        ListNode node2 = new ListNode(1, new ListNode(3, new ListNode(4)));
        ListNode node3 = new ListNode(2, new ListNode(6));
        printNode(new ListNode[]{node1, node2, node3});
    }

    private static void test2() {
        ListNode node2 = new ListNode(-1, new ListNode(5, new ListNode(11)));
        ListNode node4 = new ListNode(6, new ListNode(10));
        printNode(new ListNode[]{null, node2, null, node4});
    }

    private static void printNode(ListNode[] lists) {
        ListNode node = new _23MergeKLists().mergeKLists(lists);
        List<Integer> list = new ArrayList<>();
        while (node != null) {
            list.add(node.val);
            node = node.next;
        }
        System.out.println(list);
    }
}
