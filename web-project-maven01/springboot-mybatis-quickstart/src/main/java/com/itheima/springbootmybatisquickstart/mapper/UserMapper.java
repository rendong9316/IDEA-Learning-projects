package com.itheima.springbootmybatisquickstart.mapper;

import com.itheima.springbootmybatisquickstart.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper  //用用程序在创建的时候，会自动的创建一个UserMapper接口的实现类对象，
// 并交给spring管理，成为Ioc容器中的bean
public interface UserMapper {

    // 查询所有用户
    @Select("select * from user")
    public List<User> findAll();

}
