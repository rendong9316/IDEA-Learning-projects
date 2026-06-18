package com.itheima.jicheng;

public class SmartThings {
    String name;
    double price;

    //构造方法
    public SmartThings() {
    }

    public SmartThings(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public double priceAfterCalculate() {
        if (this.price >= 0 && this.price < 1000) {
            return this.price;
        } else if (this.price >= 1000 && this.price < 5000) {
            return this.price * 0.9;
        } else if (this.price >= 5000 && this.price < 10000) {
            return this.price * 0.8;
        } else {
            return this.price * 0.7;
        }
    }


}
