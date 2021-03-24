/**
 *
 * You are given coins of different denominations and a total amount of money amount.
 * Write a function to compute the fewest number of coins that you need to make up that amount.
 * If that amount of money cannot be made up by any combination of the coins, return -1.
 * Example 1:
 * coins = [1, 2, 5], amount = 11
 * return 3 (11 = 5 + 5 + 1)
 * Example 2:
 * coins = [2], amount = 3
 * return -1.
 * Note:
 * You may assume that you have an infinite number of each kind of coin.
 *
 */
package com.alg.leetup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rbaral
 *
 */
class CoinChangeTestCase {

    int[] coins;
    int amount;
    int coinsRequired;

    public CoinChangeTestCase(int[] coins, int amount, int coreq) {
        this.coins = coins;
        this.amount = amount;
        this.coinsRequired = coreq;
    }

    public int[] getCoins() {
        return coins;
    }

    public void setCoins(int[] coins) {
        this.coins = coins;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCoinsRequired() {
        return coinsRequired;
    }

    public void setCoinsRequired(int coinsRequired) {
        this.coinsRequired = coinsRequired;
    }

    public String toString() {
        return "For coins:" + Arrays.toString(coins) + "...for amount:" + getAmount() + "..expected return coins:" + getCoinsRequired();
    }
}

public class Coinchange {

    /**
     * @param args
     */
    public static void main(String[] args) {
        boolean isTestMode = false;
        if (isTestMode) {
            performTest();
        } else {
            int coins[];
            int amount = 0;
            int coreq = 0;
            coins = new int[]{186, 419, 83, 408};
            amount = 6249;
            //coreq = 20;
            //coins = new int[]{1,2,3};
            //amount = 5;
            //coins = new int[]{384,324,196,481};
            //amount= 285;
            coins = new int[] {2,3,6};
            amount = 7;
            //System.out.println(coinChange(coins, amount));
            //System.out.println(coinChangeRecursive(coins, amount));

            coins = new int[]{1,5,10,25};
            amount = 15;//3
            //System.out.println(coinChangeBottomUpDP(coins, amount));
            System.out.println(minimumCoinBottomUp(coins, amount));
            //Map<Integer, Integer> map = new HashMap<>();
            //System.out.println(minimumCoinTopDown(coins, amount,  map));
        }

    }

    /**
     * using DP approach
     *
     * @param coins
     * @param amount
     * @return
     */
    public static int coinChange(int[] coins, int amount) {
        //to store the used coins
        List<Integer> coinsUsed = new ArrayList<Integer>();
        //handle the base cases
        if (coins == null || coins.length == 0 || amount < 0) {
            System.out.println("coins used:" + Arrays.toString(coinsUsed.toArray()));
            return -1;
        } else if (coins.length == 1 && amount % coins[0] != 0) {//we have only one coin that doesn't divide the amount
            System.out.println("coins used:" + Arrays.toString(coinsUsed.toArray()));
            return -1;
        } else {
            //an array to hold the minimum coins required for the indexed amount,i.e. we store temp[amount] = min # of coins
            int minChanges[] = new int[amount + 1];
            minChanges[0] = 0;//no change possible for amount 0
            for (int i = 1; i <= amount; i++) {
                minChanges[i] = Integer.MAX_VALUE - 1;//let's assume we need arbitrarily large no of coins
            }
            int prev = 0, curr = 0;
            for (int i = 0; i < coins.length; i++) {
                for (int j = 1; j <= amount; j++) {
                    if (j >= coins[i]) {//if amount is at least the value of the coin, then we can find a change
                        //be careful here. Max_val + 1 can result in very small negative number.
                        prev = minChanges[j - coins[i]] + 1;
                        curr = minChanges[j];
                        minChanges[j] = Math.min(curr, prev);
                    }
                }
            }

            if (minChanges[amount] == (Integer.MAX_VALUE - 1)) {//we didn't set this element, so that means we didn't find the solution
                coinsUsed.clear();
                System.out.println("coins used:" + Arrays.toString(coinsUsed.toArray()));
                return -1;
            }
            System.out.println("coins used:" + Arrays.toString(coinsUsed.toArray()));
            return minChanges[amount];
        }
    }

    /**
     * ref:
     * http://www.mathcs.emory.edu/~cheung/Courses/323/Syllabus/DynProg/money-change.html
     *
     * @param v
     * @param Am
     * @return
     */
    public static int coinChangeBottomUpDP(int[] coins, int Am) {
        int[] minChange;
        int[] sol, mySol;
        int j, k;
        minChange = new int[Am + 1]; // Store results
        sol = new int[coins.length];
        mySol = new int[coins.length];
        List<Integer> coinsUsed = new ArrayList<Integer>();
        /*
		 * Base case 
         */
        minChange[0] = 0; // 0 coins needed to make change for $0
        /*
		 * --------------------------------------------------- The other cases
		 * (starting from 1 to M.length - 1)
		 * 
		 * Follow direction of data flow !
		 * ---------------------------------------------------
         */
        for (j = 1; j <= Am; j++) {
            /*
			 * Find min # coin to make change for $j 
             */

            for (k = 0; k < coins.length; k++) {
                mySol[k] = -1; // Initialize mySol[]
            }
            /*
			 *  Try every denomination k = 1,2,..,C for the last coin
			 * --------------------------------------------------------
             */
            for (k = 0; k < coins.length; k++) {
                /*
				 * -- Check if we can use the k-th denomination, for this amount
				 * --------------------------------------------
                 */
                if (coins[k] <= j) {
                    /*
					 *  Divide step ---
                     */
                    sol[k] = minChange[j - coins[k]]; // Use coin coins[k] as the last coin
                    mySol[k] = sol[k] + 1; // Solution to make change for $j
                }
            }
            //System.out.println(Arrays.toString(mySol));
            /*
			 *  Find the minimum of all mySol[...]
			 * --------------------------------------------------------
             */
            minChange[j] = -1;

            for (k = 0; k < coins.length; k++) {
                if (mySol[k] > 0) {
                    if (minChange[j] == -1 || mySol[k] < minChange[j]) {
                        minChange[j] = mySol[k];
                    }
                }
            }
        }
        System.out.println("Coins used:" + Arrays.toString(coinsUsed.toArray()) + "...." + minChange[Am]);
        return minChange[Am]; // Min # coins to change for $Am
    }

    /**
     * ref:https://github.com/mission-peace/interview/blob/master/src/com/interview/dynamic/CoinChangingMinimumCoin.java
     *
     * @param arr
     * @param total
     * @return
     */
    public static int minimumCoinBottomUp(int coins[], int total) {
        int temp[] = new int[total + 1];
        temp[0] = 0;
        List<List<Integer>> coinsUsed = new ArrayList<List<Integer>>();
        coinsUsed.add(new ArrayList<Integer>());
        for (int i = 1; i <= total; i++) { //lets suppose for every value from 1 to total, we need some number of coins
            temp[i] = Integer.MAX_VALUE - 1;
            coinsUsed.add(new ArrayList<Integer>());
        }
        
        
        for (int j = 1; j <= total; j++) { //the amount
            for (int i = 0; i < coins.length; i++) {//all denominations
                if (j >= coins[i]) {
                    //be careful here. Max_val + 1 can result in very small negative number.
                    temp[j] = Math.min(temp[j], temp[j - coins[i]] + 1);
                    //for this amount, we used the coins=> coins[i]*1 + coinsUsed[j - coins[i]]
                    List<Integer> denom = new ArrayList<Integer>();
                    denom.addAll(coinsUsed.get(j-coins[i]));
                    denom.add(coins[i]);
                    coinsUsed.set(j, denom);
                } 
            }
            
        }
        for(int i=0;i<coinsUsed.size();i++){
            System.out.println("coins used for amount:"+i+" are:"+Arrays.toString(coinsUsed.get(i).toArray()));
        }
        return temp[total];
    }
    
    
    
    

    /**
     * Top down dynamic programing. Using map to store intermediate results.
     *
     */
    /*public static int minimumCoinTopDown( int coins[],int total, Map<Integer, Integer> map) {

		//if total is 0 then there is nothing to do. return 0.
        if ( total == 0 ) {
            return 0;
        }

        //if map contains the result means we calculated it before. Lets return that value.
        if ( map.containsKey(total) ) {
            return map.get(total);
        }

        //iterate through all coins and see which one gives best result.
        int min = Integer.MAX_VALUE;
        for ( int i=0; i < coins.length; i++ ) {
            //if value of coin is greater than total we are looking for just continue.
            if( coins[i] > total ) {
                continue;
            }
            //recurse with total - coins[i] as new total
            int val = minimumCoinTopDown(coins,total - coins[i], map);

            //if val we get from picking coins[i] as first coin for current total is less
            // than value found so far make it minimum.
            if( val < min ) {
                min = val;
            }
        }

        //if min is MAX_VAL dont change it. Just result it as is. Otherwise add 1 to it.
        min =  (min == Integer.MAX_VALUE ? min : min + 1);

        //memoize the minimum for current total.
        map.put(total, min);
        return min;
    }*/
    public static void performTest() {
        List<CoinChangeTestCase> testCases = new ArrayList<CoinChangeTestCase>();
        List<String> failedCases = new ArrayList<String>();
        int coins[];
        int amount = 0;
        int coreq = 0;
        coins = new int[]{1, 2, 5};
        amount = 11;
        coreq = 3;//5,5,2
        testCases.add(new CoinChangeTestCase(coins, amount, coreq));
        coins = new int[]{2};
        amount = 3;
        coreq = -1;// not possible to make changes
        testCases.add(new CoinChangeTestCase(coins, amount, coreq));
        coins = new int[]{186, 419, 83, 408};
        amount = 6249;
        coreq = 20;
        testCases.add(new CoinChangeTestCase(coins, amount, coreq));
        coins = new int[]{1, 2, 3};
        amount = 5;
        testCases.add(new CoinChangeTestCase(coins, amount, 2));
        coins = new int[]{384, 324, 196, 481};
        amount = 285;
        testCases.add(new CoinChangeTestCase(coins, amount, -1));
        int result = 0;

        for (CoinChangeTestCase testCase : testCases) {
            //result = coinChange(testCase.getCoins(), testCase.getAmount());
            //Map<Integer, Integer> map = new HashMap<>();
            result = coinChangeBottomUpDP(testCase.getCoins(), testCase.getAmount());
            //result = minimumCoinTopDown(coins,amount, map);
            if (result != testCase.getCoinsRequired()) {
                failedCases.add("FAILED FOR:" + testCase.toString() + "...got:" + result);
            }
        }
        System.out.println("TEST RESULT: PASSED:" + (testCases.size() - failedCases.size()) + "...FAILED:" + failedCases.size());
        for (String res : failedCases) {
            System.err.println(res);
        }

    }

}
