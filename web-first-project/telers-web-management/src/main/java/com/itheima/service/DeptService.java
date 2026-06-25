package com.itheima.service;

import com.itheima.pojo.Dept;

import java.util.List;

public interface DeptService {
    //查询所有部门数据（alt加回车快速创建出来）
    List<Dept> findAll();

    void delete(Integer id);
}
