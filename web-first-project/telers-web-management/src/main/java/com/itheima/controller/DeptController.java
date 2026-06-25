package com.itheima.controller;

import com.itheima.pojo.Dept;
import com.itheima.pojo.Result;
import com.itheima.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
//restcontroller注解的作用是返回json数据
@RestController
public class DeptController {

    @Autowired
    private DeptService deptService;


    @GetMapping("/depts")
    public Result list() {
        System.out.println("查询全部部门数据");
        List<Dept> deptList = deptService.findAll();
        return Result.success(deptList);

    }

    @DeleteMapping("/depts")
    public Result delete(Integer id) {
        System.out.println("删除部门数据：" + id);
        deptService.delete(id);
        return Result.success();
    }
}
