package com.itheima.jicheng;

public class Test {
    public static void main(String[] args) {
        Phone phone = new Phone("手机", 2000, 10);
        System.out.println(phone.priceAfterCalculate());

        Laptop laptop = new Laptop();
        laptop.name = "笔记本";
        laptop.price = 10000;
        System.out.println(laptop.priceAfterCalculate());

    }
}
