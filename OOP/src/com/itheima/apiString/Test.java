package com.itheima.apiString;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) throws IOException {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Tool.outString(arr);
        System.out.println(Math.random()*100-50);
        System.out.println(Runtime.getRuntime().totalMemory()/1024/1024);
        //Runtime.getRuntime().exec("notepad");
        Random random = new Random();
        BigInteger bigInteger = new BigInteger(100000, random);
        System.out.println(bigInteger);

        System.out.println("------------------------------");
        TreeSet<Integer> treeSet = new TreeSet<>();
        treeSet.add(1);
        treeSet.add(2);
        treeSet.add(5);
        treeSet.add(3);
        treeSet.add(4);
        System.out.println(treeSet);
        System.out.println("♥");
        Stream.of(1,2,3,4,5,"a").forEach(s->System.out.println(s));








    }
}
