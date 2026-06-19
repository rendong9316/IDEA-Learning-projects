package com.itheima.springbootmybatisquickstart;


import com.itheima.springbootmybatisquickstart.mapper.UserMapper;
import com.itheima.springbootmybatisquickstart.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringbootMybatisQuickstartApplicationTests {
        @Autowired
        private UserMapper userMapper;
        // 测试查询所有用户
        @Test
        public void testFindAll(){
            List<User> users = userMapper.findAll();
            users.forEach(user -> System.out.println(user));
        }

        //测试删除某个id的用户
        @Test
        public void testDeleteById(){
            userMapper.deleteById(1);

        }

        //测试添加用户
        @Test
        public void testAddUser(){
            User user = new User();
            user.setId(3);
            user.setUsername("小li");
            user.setGender("女");

            userMapper.addUser(user);
        }


}
