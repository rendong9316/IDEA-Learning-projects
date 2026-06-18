package com.itheima.springbootwebquickstart;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController   //请求处理类
public class HelloControler {
    @RequestMapping("/hello")
    public String hello(String name){
        System.out.println("name :"+ name);
        return "Hello" + name + "!";
    }
}
