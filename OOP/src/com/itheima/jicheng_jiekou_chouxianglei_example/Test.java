package com.itheima.jicheng_jiekou_chouxianglei_example;

public class Test {
    public static void main(String[] args) {
        Coach_PingPang coachPingPang = new Coach_PingPang("zhangsan",18);
        coachPingPang.teach();
        coachPingPang.setAge(20);
        coachPingPang.speakEnglish();
        System.out.println(coachPingPang.getAge());

        Athlete_PingPang athletePingPang = new Athlete_PingPang("lisi",19);
        athletePingPang.study();
        //athletePingPang.setName("wangwu");
        System.out.println(athletePingPang.getName());
        athletePingPang.speakEnglish();

    }
}
