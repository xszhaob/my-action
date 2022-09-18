package algorithm.leetcode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author zhaobo
 * @Description
 * @date 2022/9/12 15:56
 */
public class _144PreOrderTraversal {

    public List<Integer> preOrderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        doPreOrderTraversal(root, result);

        return result;
    }


    public void doPreOrderTraversal(TreeNode root, List<Integer> result) {
        if (root == null) {
            return;
        }

        result.add(root.val);

        doPreOrderTraversal(root.left, result);
        doPreOrderTraversal(root.right, result);
    }


    public static void main(String[] args) {
        TreeNode node3 = new TreeNode();
        node3.val = 3;

        TreeNode node2 = new TreeNode();
        node2.val = 2;

        node2.left = node3;

        TreeNode root = new TreeNode();
        root.val = 1;
        root.right = node2;

        System.out.println(new _144PreOrderTraversal().preOrderTraversal(root));
    }
}
