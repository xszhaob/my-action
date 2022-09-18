package algorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaobo
 * @Description 二叉树的中序遍历：在二叉树中，中序遍历首先遍历左子树，然后访问根结点，最后遍历右子树。
 * @date 2022/9/12 15:13
 */
public class _94TreeNodeInorderTraversal {

    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        doInorderTraversal(root, result);
        return result;
    }


    public void doInorderTraversal(TreeNode root, List<Integer> result) {
        if (root == null) {
            return;
        }
        doInorderTraversal(root.left, result);
        result.add(root.val);
        doInorderTraversal(root.right, result);
    }


    private static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode() {
        }

        public TreeNode(int val) {
            this.val = val;
        }

        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}
