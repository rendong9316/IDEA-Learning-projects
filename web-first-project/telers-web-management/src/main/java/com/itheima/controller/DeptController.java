package com.itheima.controller;

import com.itheima.pojo.Dept;
import com.itheima.pojo.Result;
import com.itheima.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//restcontroller注解的作用是返回json数据
@RestController

@Slf4j
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

    @PostMapping("/depts")
    public Result add(@RequestBody Dept dept) {
        System.out.println("添加部门数据：" + dept);
        deptService.add(dept);
        return Result.success();
    }

    @GetMapping("/depts/{id}")
    public Result findById(@PathVariable Integer id) {
        System.out.println("查询部门数据：" + id);
        Dept dept = deptService.findById(id);
        return Result.success(dept);
    }

}
