package com.itheima.circle;

public class Circle {
    //属性
    private int r;
    private final double PAI =3.1415926535;

    //构造方法
    public Circle() {
    }
    public Circle(int r) {
        this.r = r;
    }

    //get set方法
    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public double getPAI() {
        return PAI;
    }

    //行为
    public double getArea() {
        return PAI * r * r;
    }
    public double getCircumference() {
        return 2 * PAI * r;
    }
}
