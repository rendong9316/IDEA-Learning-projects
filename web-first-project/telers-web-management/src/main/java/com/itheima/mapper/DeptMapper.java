package com.itheima.mapper;


import com.itheima.pojo.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DeptMapper {
    //查询所有部门数据（也可快捷创建）

    @Select("select id, name, create_time, update_time from dept order by update_time desc")
    List<Dept> findAll();

    @Select("delete from dept where id = #{id}")
    void delete(Integer id);

    @Select("insert into dept(name, create_time, update_time) values(#{name}, #{createTime}, #{updateTime})")
    void add(Dept dept);
}
