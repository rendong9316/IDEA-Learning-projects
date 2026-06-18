package com.itheima.jicheng_jiekou_chouxianglei_example;

public class Athlete_Basketball extends Althlete{
    @Override
    public void study() {
        System.out.println("篮球运动员学习篮球");
    }

    public Athlete_Basketball() {
    }

    public Athlete_Basketball(String name, int age) {
        super(name, age);
    }
}
