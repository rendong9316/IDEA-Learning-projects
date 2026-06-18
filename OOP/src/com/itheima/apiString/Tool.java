package com.itheima.apiString;

public class Tool {
    private Tool() {
    }
    public static void outString(int [] str) {
        String arr="[";
        for(int i=0;i<str.length-1;i++){
            arr+=str[i]+",";
        }
        arr+=str[str.length-1]+"]";
        System.out.println(arr);
        System.out.println(55&56);

    }

}
