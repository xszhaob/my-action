package algorithm.leetcode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author zhaobo
 * @Description 二叉树的锯齿形层序遍历：即先从左往右，再从右往左进行下一层遍历，以此类推，层与层之间交替进行
 * @date 2022/9/12 15:31
 */
public class _103TreeNodeZigzagLevelOrder {
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();

        if (root == null) {
            return result;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int count = queue.size();
        boolean leftToRight = true;

        while (!queue.isEmpty()) {
            List<Integer> levelValList = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                TreeNode node = queue.poll();
                if (leftToRight) {
                    levelValList.add(node.val);
                } else {
                    levelValList.add(0, node.val);
                }
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }

            result.add(levelValList);

            count = queue.size();
            leftToRight = !leftToRight;
        }

        return result;
    }
}
