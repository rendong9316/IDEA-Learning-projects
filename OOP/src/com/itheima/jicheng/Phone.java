package com.itheima.jicheng;

public class Phone extends SmartThings {
    int gNum;
    //构造方法
    public Phone() {
    }
    public Phone(String name, double price, int gNum) {
        super(name, price);
        this.gNum = gNum;
    }

    @Override
    public double priceAfterCalculate() {
        return 0.9*super.priceAfterCalculate();
    }
}
