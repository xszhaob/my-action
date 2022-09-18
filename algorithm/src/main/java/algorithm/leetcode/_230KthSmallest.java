package algorithm.leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author zhaobo
 * @Description
 * @date 2022/9/12 14:38
 */
public class _230KthSmallest {

    public static int kthSmallest(TreeNode root, int k) {
        int mark = 1;
        Stack<TreeNode> stack = new Stack<>();
        while (!stack.isEmpty() || root != null) {
            if (root != null) {
                stack.push(root);
                root = root.left;
            } else {
                root = stack.pop();
                if (k == mark) {
                    return root.val;
                } else {
                    mark++;
                }
                root = root.right;
            }
        }
        return 0;
    }

    public int kthSmallest1(TreeNode root, int k) {
        if (root == null) {
            return -1;
        }

        List<Integer> valueList = new ArrayList<>();
        inOrderTraversal(root, valueList, k);
        return valueList.get(k - 1);

    }


    public void inOrderTraversal(TreeNode root, List<Integer> result, int k) {
        if (root == null) {
            return;
        }
        if (result.size() == k - 1) {
            return;
        }

        inOrderTraversal(root.left, result, k);
        result.add(root.val);
        inOrderTraversal(root.right, result, k);
    }
}
