package algorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaobo
 * @Description
 * @date 2022/9/12 16:12
 */
public class _105TreeNodeBuildTree {

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        if (preorder == null || inorder == null) {
            return null;
        }

        List<Integer> preorderList = new ArrayList<>(preorder.length);
        for (int i : preorder) {
            preorderList.add(i);
        }

        List<Integer> inorderList = new ArrayList<>(inorder.length);
        for (int i : inorder) {
            inorderList.add(i);
        }

        return doBuild(preorderList, inorderList);
    }

    private TreeNode doBuild(List<Integer> preorderList, List<Integer> inorderList) {
        if (preorderList.isEmpty()) {
            return null;
        }
        Integer rootVal = preorderList.get(0);

        preorderList.remove(0);

        int index = inorderList.indexOf(rootVal);
        List<Integer> leftNodeValList = inorderList.subList(0, index);
        List<Integer> rightNodeValList = new ArrayList<>();
        if (index + 1 < inorderList.size()) {
            rightNodeValList = inorderList.subList(index + 1, inorderList.size());
        }

        TreeNode root = new TreeNode();
        root.val = rootVal;
        if (!leftNodeValList.isEmpty()) {
            root.left = doBuild(preorderList, leftNodeValList);
        }
        if (!rightNodeValList.isEmpty()) {
            root.right = doBuild(preorderList, rightNodeValList);
        }
        return root;
    }


    public static void main(String[] args) {
        System.out.println(new _105TreeNodeBuildTree().buildTree(new int[]{3,9,20,15,7}, new int[]{9,3,15,20,7}));
    }
}
