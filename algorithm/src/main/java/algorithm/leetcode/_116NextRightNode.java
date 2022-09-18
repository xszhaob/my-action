package algorithm.leetcode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author zhaobo
 * @Description
 * @date 2022/9/12 19:05
 */
public class _116NextRightNode {
    public static Node connect(Node root) {
        if (root == null) {
            return null;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);


        while (!queue.isEmpty()) {
            int count = queue.size();
            Node preNode = null;
            for (int i = 0; i < count; i++) {
                Node curNode = queue.poll();
                if (preNode != null) {
                    preNode.next = curNode;
                }
                preNode = curNode;
                if (curNode.left != null) {
                    queue.add(curNode.left);
                }
                if (curNode.right != null) {
                    queue.add(curNode.right);
                }
            }
        }
        return root;
    }


    public static void main(String[] args) {
        Node node4 = new Node(4, null, null, null);
        Node node5 = new Node(5, null, null, null);
        Node node6 = new Node(6, null, null, null);
        Node node7 = new Node(7, null, null, null);
        Node node2 = new Node(2, node4, node5, null);
        Node node3 = new Node(3, node6, node7, null);
        Node node1 = new Node(1, node2, node3, null);
        System.out.println(connect(node1));
    }
}
