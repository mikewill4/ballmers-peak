import java.util.*;
import java.util.Random;

public class Tester {
    public static void main(String[] args) {
        System.out.println(testStockIncreases());
        System.out.println(test6DMahjong());
        System.out.println(testSki());
    }

    // Runtime should be O(n  ^ 2)
    public static boolean canIWinCorrect(int[] board) {
        if (board == null || board.length == 0) {
            return false;
        }

        return helper(board, 0, board.length-1, new Integer[board.length][board.length])>=0;
    }

    private static int helper(int[] nums, int s, int e, Integer[][] mem){    
        if(mem[s][e]==null)
            mem[s][e] = s==e ? nums[e] : Math.max(nums[e]-helper(nums,s,e-1,mem),nums[s]-helper(nums,s+1,e,mem));
        return mem[s][e];
    }

    // Runtime should be O(n)
    public static int[] stockIncreasesCorrect(int[] prices) {
        if (prices.length == 0) {
            return prices;
        }

        int[] result = new int[prices.length];
        Stack<Integer> stck = new Stack<>();

        for (int i = 0; i < prices.length; i++) {
            while(!stck.isEmpty() && prices[stck.peek()] < prices[i]) {
                result[stck.peek()] = i - stck.peek();
                stck.pop();
            }

            stck.push(i);
        }

        return result;
    }

    // Runtime should be O(n)
    public static int skiCorrect(int[] mountains) {
        if (mountains == null || mountains.length == 0) {
            return 0;
        }

        int max = 0;
        int curr = 0;

        for (int i = 0; i < mountains.length; i++) {
            if (mountains[i] > 0) {
                curr += mountains[i];
                max = Math.max(max, curr);
            } else {
                curr = 0;
            }
        }

        return max;
    }

    private static int testSki() {
        int numCorrect = 0;

        for (int i = 0; i < 100; i++) {
            int[] test = generateRandomPositiveIntArray();
            int result = skiCorrect(test);
            try {
                int theirResult = ski(test);
                if (result == theirResult) {
                    numCorrect++;
                } else {
                    printArray(test);
                }
            } catch (Exception e) {
                printArray(test);
                continue;
            }
        }

        return numCorrect;
    }

    private static int testStockIncreases() {
        int numCorrect = 0;

        for (int i = 0; i < 100; i++) {
            int[] test = generateRandomPositiveIntArray();
            int[] result = stockIncreasesCorrect(test);
            try {
                int[] theirResult = stockIncreases(test);
                if (compareTwoArrays(result, theirResult)) {
                    numCorrect++;
                } else {
                    printArray(test);
                }
            } catch (Exception e) {
                printArray(test);
                continue;
            }
        }

        return numCorrect;
    }

    private static int test6DMahjong() {
        int numCorrect = 0;

        for (int i = 0; i < 100; i++) {
            int[] test = generateRandomPositiveIntArray();
            boolean result = canIWinCorrect(test);

            try {
                boolean theirResult = canIWin(test);
                if (result == theirResult) {
                    numCorrect++;
                } else {
                    printArray(test);
                }
            } catch (Exception e) {
                printArray(test);
                continue;
            }
        }

        return numCorrect;
    }

    private static int[] generateRandomPositiveIntArray() {
        Random rand = new Random();
        int length = rand.nextInt(50) + 1;
        int[] test = new int[length];

        for (int j = 0; j < length; j++) {
            test[j] = rand.nextInt(100);
        }

        return test;
    }

    private static void printArray(int[] arr) {
        System.out.print("{");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);

            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }

        System.out.print("}");
        System.out.println();
    }

    private static boolean compareTwoArrays(int[] first, int[] second) {
        if (first == null && second == null) {
            return true;
        }

        if (first == null || second == null) {
            return false;
        }

        if (first.length != second.length) {
            return false;
        }

        for (int i = 0; i < first.length; i++) {
            if (first[i] != second[i]) {
                return false;
            }
        }

        return true;
    }

    private static int[] stockIncreases(int[] input) {
        return null;
    }

    private static boolean canIWin(int[] board) {
        return false;
    }

    private static int ski(int[]mountains) {
        return -1;
    }
}