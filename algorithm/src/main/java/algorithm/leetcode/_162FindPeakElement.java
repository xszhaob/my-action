package algorithm.leetcode;

/**
 * @author zhaobo
 * @Description
 * @date 2022/9/24 23:54
 */
public class _162FindPeakElement {
    public int findPeakElement(int[] nums) {
        return binarySearch(0, nums.length - 1, nums);
    }

    public int binarySearch(int begin, int end, int[] nums) {
        int mid = begin + (end - begin) / 2;

        if (begin < end) {
            if (nums[mid] > nums[mid + 1]) {
                return binarySearch(begin, mid, nums);
            } else {
                return binarySearch(mid + 1, end, nums);
            }
        }
        return mid;
    }


    public static void main(String[] args) {
        System.out.println(new _162FindPeakElement().findPeakElement(new int[]{6,5,4,3,2,3,2}));
    }
}
