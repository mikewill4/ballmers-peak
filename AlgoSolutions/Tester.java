import java.util.*;
import java.util.Random;
import java.lang.*;

public class Tester {
    public static void main(String[] args) {
        // System.out.println(testStockIncreases());
        // System.out.println(test6DMahjong());
        // System.out.println(testSki());
        // System.out.println(testRound());
        // System.out.println(testBrainCells());
        // System.out.println(testLogOrder());
        // System.out.println(testKidsLiningUp());
    }

    // Should be O(n log n)
    public static int getMinNumberLinesCorrect(int[] heights) {
        return lengthOfLIS(heights);
    }

    public static int lengthOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int[] tails = new int[nums.length];
        int longest = 0;
        
        for (int num : nums) {
            int left = 0;
            int right = longest;
            
            while (left < right) {
                int mid = (left + right) / 2;
                
                if (tails[mid] < num) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            
            if (left == longest || nums[left] == num) {
                longest++;
            }
            
            tails[left] = num;
        }
        
        return longest;
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

    // Runtime should be O(n)
    private static int[] roundCorrect(double[] input, int target) {
        int flooredSum = 0;
        PriorityQueue<double[]> maxHeap = new PriorityQueue<>((a,b) -> Double.compare(b[0], a[0]));
        int[] results = new int[input.length];

        for (int i = 0; i < input.length; i++) {
            int floored = (int) Math.floor(input[i]);
            results[i] = floored;
            flooredSum += floored;
            double[] element = new double[] {input[i] - floored, i};
            maxHeap.offer(element);
        }

        int difference = target - flooredSum;

        for (int i = 0; i < difference && !maxHeap.isEmpty(); i++) {
            double[] top = maxHeap.poll();
            int index = (int) top[1];

            results[index] = (int) Math.ceil(input[index]);
        }

        return results;
    }

    // Runtime should be roughly O(n ^ 2)
    private static boolean canIFulfillOrderCorrect(int[] inventory, int[] order) {
        List<Integer> inv = new ArrayList<>();
        for (int i = 0; i < inventory.length; i++) {
            inv.add(inventory[i]);
        }

        List<Integer> orders = new ArrayList<>();
        for (int i = 0; i < order.length; i++) {
            orders.add(order[i]);
        }

        Collections.sort(orders);
        return fulfillOrderHelper(inv, orders);
    }

    private static boolean fulfillOrderHelper(List<Integer> inv, List<Integer> order) {
        if (order.size() == 0) {
            return true;
        }

        if (inv.size() == 0) {
            return false;
        }

        int maxOrder = order.get(order.size() - 1);
        order.remove(order.size() - 1);
        
        for (int i = 0; i < inv.size(); i++) {
            if (inv.get(i) >= maxOrder) {
                List<Integer> next = new ArrayList<>(inv);
                next.remove(i);

                if (fulfillOrderHelper(next, new ArrayList<>(order))) {
                    return true;
                }
            }
        }

        return false;
    }

    private static int testLogOrder() {
        int numCorrect = 0;
        for (int i = 0; i < 100; i++) {
            int[] inventory = generateRandomPositiveIntArray();
            int[] order = generateRandomPositiveIntArray();
            boolean result = canIFulfillOrderCorrect(inventory, order);
            
            try {
                boolean theirResult = canIFulfillOrder(inventory, order);
                if (result == theirResult) {
                    numCorrect++;
                } 
            } catch (Exception e) {
                continue;
            }
        }

        return (int) ((1.0 * numCorrect) / 100 * 150);
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

        return (int) ((1.0 * numCorrect) / 100 * 50);
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

        return (int) ((1.0 * numCorrect) / 100 * 50);
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

        return (int) ((1.0 * numCorrect) / 100 * 100);
    }

    private static int testKidsLiningUp() {
        int numCorrect = 0;

        for (int i = 0; i < 100; i++) {
            int[] test = generateRandomPositiveIntArray();
            int result = getMinNumberLinesCorrect(test);

            try {
                int theirResult = getMinNumberLines(test);
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

        return (int) ((1.0 * numCorrect) / 100 * 100);
    }

    private static int testRound() {
        int numCorrect = 0;

        for (int i = 0; i < 100; i++) {
            int[] test = generateRandomPositiveIntArray();
            double[] converted = new double[test.length];
            int sum = 0;

            for (int j = 0; j < test.length; j++) {
                sum += test[j];
                converted[j] = test[j] + Math.random();
            }
            
            int[] result = roundCorrect(converted, sum);

            try {
                int theirSum = 0;
                double theirRoundOff = 0;
                double ourRoundOff = 0;
                int[] theirResult = round(converted, sum);

                if (theirResult == null || theirResult.length != result.length) {
                    continue;
                }

                for (int j = 0; j < theirResult.length; j++) {
                    theirSum += theirResult[j];
                    theirRoundOff += Math.abs(theirResult[j] - converted[j]);
                    ourRoundOff += Math.abs(result[j] - converted[j]);
                }

                if (theirSum == sum && Double.compare(theirRoundOff, ourRoundOff) == 0) {
                    numCorrect++;
                }
            } catch (Exception e) {
                printArray(test);
                continue;
            }
        }

        return (int) ((1.0 * numCorrect) / 100 * 100);
    }

    private static int testBrainCells() {
        int numCorrect = 0;

        String[] in1 = new String[] {"Test", "Tim", "Tim"};
        String[] in2 = new String[] {"A", "B", "C", "C", "B", "E", "E"};
        String[] in3 = new String[] {"A", "B", "A", "A", "A", "A"};
        String[] in4 = new String[] {"A"};
        String[] in5 = new String[] {"A", "A", "B", "C", "C", "D", "A", "C,", "E", "C"};

        try {
            if (maxAssignments(in1, 1) == 2) {
                numCorrect++;
            }    
        } catch (Exception e){}

        try {
            if (maxAssignments(in2, 2) == 4) {
                numCorrect++;
            }
        } catch (Exception e) {}

        try {
            if (maxAssignments(in1, 2) == 3) {
                numCorrect++;
            }
        } catch (Exception e) {}

        try {
            if (maxAssignments(in3, 2) == 6) {
                numCorrect++;
            }
        } catch (Exception e) {}

        try {
            if (maxAssignments(in3, 1) == 4) {
                numCorrect++;
            }
        } catch (Exception e) {}

        try {
            if (maxAssignments(in4, 5) == 1) {
                numCorrect++;
            }
        } catch (Exception e) {}

        try {
            if (maxAssignments(in5, 3) == 5) {
                numCorrect++;
            }
        } catch (Exception e) {}

        return (int) ((1.0 * numCorrect) / 7 * 150);
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

    private static int[] round(double[] input, int target) {
        return null;
    }

    private static boolean canIFulfillOrder(int[] inventory, int[] order) {
        return false;
    }

    private static int maxAssignments(String[] words, int numBrainCells) {
        return -1;
    }

    private static int getMinNumberLines(int[] children) {
        return -1;
    }
}