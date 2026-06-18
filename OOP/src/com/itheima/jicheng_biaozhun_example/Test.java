package com.itheima.jicheng_biaozhun_example;

public class Test {
    public static void main(String[] args) {
        Undergraduate undergraduate = new Undergraduate();
        undergraduate.setName("小王");
        undergraduate.setAge(18);
        undergraduate.setGrade("2020级");
        System.out.println("姓名："+undergraduate.getName());
        System.out.println("年龄："+undergraduate.getAge());
        System.out.println("年级："+undergraduate.getGrade());
        System.out.println("------------------------------------------------");
        undergraduate.eat();
        undergraduate.study();
        undergraduate.sleep();


    }
}
