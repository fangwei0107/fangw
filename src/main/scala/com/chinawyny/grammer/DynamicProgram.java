package com.chinawyny.grammer;

import java.util.Date;

public class DynamicProgram {
    private int solution(int n) {
        if (n >= 0 && n <= 2) {
            return n;
        }
        return solution(n - 1) + solution(n - 2);
    }

    private int solution1(int n) {
        int[] dp = new int[n];
        return s1(n, dp);

    }

    private int s1(int n, int[] dp) {
        if (n >= 0 && n <= 2) {
            return n;
        }

        if (dp[n] != 0) {
            return dp[n];
        }

        dp[n] = solution(n - 1) + solution(n - 2);

        return dp[n];
    }

    private int solution3(int n) {
        int dp[] = new int[n+1];
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3;i <= n; i++) {
            dp[n] = dp[n-1] + dp[n -2];
        }
        return dp[n];
    }

    public static void main(String[] args) {
        DynamicProgram dynamicProgram = new DynamicProgram();
        Date d1 = new Date();
        System.out.println(dynamicProgram.solution(10));
        Date d2 = new Date();
        System.out.println(d2.getTime() - d1.getTime());
        System.out.println(dynamicProgram.solution1(10));
        Date d3 = new Date();
        System.out.println(d3.getTime() - d2.getTime());
    }
}
