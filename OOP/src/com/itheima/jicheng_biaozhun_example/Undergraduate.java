package com.itheima.jicheng_biaozhun_example;

public class Undergraduate extends Student{
    @Override
    public void study() {
        System.out.println("本科生学习");
    }

    public Undergraduate() {
    }

    public Undergraduate(String name, int age, String grade) {
        super(name, age, grade);
    }
}
