package com.wx.genealogy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class  Main{

    public static void main(String[] args) {
        List<Integer> left1 = new ArrayList<>(createList(444, 443, 442, 441, 329, 328, 198, 197, 50, 49, 48, 40, 39, 35, 33, 31, 28, 24, 20, 19));
        List<Integer> right1 = new ArrayList<>(createList(93, 92, 88, 87, 86, 85, 84, 51, 50, 49, 48, 40, 39, 35, 33, 31, 28, 24, 20, 19));

        List<Integer> left2 = new ArrayList<>(createList(444, 443, 442, 441, 329, 328, 198, 197, 50, 49, 48, 40, 39, 35, 33, 31, 28, 24, 20, 19));
        List<Integer> right2 = new ArrayList<>(createList(386, 385, 384, 383, 382, 381, 380, 379, 378, 49, 48, 40, 39, 35, 33, 31, 28, 24, 20, 19));

        int distance1 = calculateDistance(left1, right1);
        int distance2 = calculateDistance(left2, right2);

        System.out.println("Distance 1: " + distance1);
        System.out.println("Distance 2: " + distance2);
    }

    private static List<Integer> createList(Integer... elements) {
        return Arrays.asList(elements);
    }

    public static int calculateDistance(List<Integer> left, List<Integer> right) {
        int distance = 0;
        int minLength = Math.min(left.size(), right.size());

        for (int i = 0; i < minLength; i++) {
            if (!left.get(i).equals(right.get(i))) {
                distance = i + 1; // Distance is 1-based
            }
        }

        return distance;
    }
}