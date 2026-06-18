package com.itheima.jicheng_jiekou_chouxianglei_example;

public class Coach_PingPang extends Coach implements Inter_EnglishSpeaking{
    @Override
    public void speakEnglish() {
        System.out.println("乒乓球教练说英语");
    }

    @Override
    public void teach() {
        System.out.println("乒乓球教练教打乒乓球");
    }

    public Coach_PingPang() {
    }

    public Coach_PingPang(String name, int age) {
        super(name, age);
    }
}
