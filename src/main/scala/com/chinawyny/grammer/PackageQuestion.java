package com.chinawyny.grammer;

public class PackageQuestion {
    int rec(int i, int n, int maxWeight, int[] weights, int[] values) {
        int res;
        if (i == n) {
            res = 0;
        } else if (maxWeight < values[i]) {
            res = rec(i + 1, n, maxWeight, weights, values);
        } else {
            res = Math.max(rec(i + 1, n, maxWeight, weights, values),
                    rec(i + 1, n, maxWeight - weights[i], weights, values) + values[i]);
        }
        return res;
    }

    public static void main(String[] args) {
        PackageQuestion packageQuestion = new PackageQuestion();
        System.out.println(packageQuestion.rec(0, 4, 5, new int[]{2, 1, 3, 2}, new int[]{3, 2, 4, 2}));
    }
}
