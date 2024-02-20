package algorithm.leetcode;

/**
 * @author zhaobo
 * @Description
 * @date 2022/9/25 19:42
 */
public class _33Search {

    public int search(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        if (nums.length == 1) {
            if (nums[0] == target) {
                return 0;
            } else {
                return -1;
            }
        }

        int index = -1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i - 1] > nums[i]) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return binarySearch(nums, target, 0, nums.length - 1);
        }

        int result = binarySearch(nums, target, 0, index - 1);
        if (result != -1) {
            return result;
        }

        result = binarySearch(nums, target, index, nums.length - 1);
        return result;
    }

    private int binarySearch(int[] nums, int target, int start, int end) {
        if (start > end) {
            return -1;
        }
        int mid = start + (end - start) / 2;

        if (nums[mid] == target) {
            return mid;
        } else if (target > nums[mid]) {
            return binarySearch(nums, target, mid + 1, end);
        } else {
            return binarySearch(nums, target, start, mid - 1);
        }
    }


    public static void main(String[] args) {
        System.out.println(new _33Search().search(new int[]{4,5,6,7,0,1,2}, 0));
        System.out.println(new _33Search().search(new int[]{4,5,6,7,0,1,2}, 2));
        System.out.println(new _33Search().search(new int[]{4,5,6,7,0,1,2}, 8));
        System.out.println(new _33Search().search(new int[]{1}, 0));
        System.out.println(new _33Search().search(new int[]{1, 3}, 1));
    }
}
