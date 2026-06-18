package com.itheima.jicheng_biaozhun_example;

public class Graduate extends  Student{
    @Override
    public void study() {
        System.out.println("研究生学习");
    }

    public Graduate() {
    }

    public Graduate(String name, int age, String grade) {
        super(name, age, grade);
    }
}
