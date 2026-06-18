package com.itheima.jicheng_jiekou_chouxianglei_example;

public class Athlete_PingPang extends Althlete implements Inter_EnglishSpeaking{
    @Override
    public void speakEnglish() {
        System.out.println("乒乓球运动员说英语");
    }

    @Override
    public void study() {
        System.out.println("乒乓球运动员学习乒乓球");
    }

    public Athlete_PingPang() {
    }

    public Athlete_PingPang(String name, int age) {
        super(name, age);
    }
}
