package com.itheima.jicheng_biaozhun_example;

public class GeneralEducationTeacher extends Teacher{
    @Override
    public void teach() {
        System.out.println("教普通课程");
    }

    public GeneralEducationTeacher() {
    }

    public GeneralEducationTeacher(String name, int age, String subject) {
        super(name, age, subject);
    }
}
