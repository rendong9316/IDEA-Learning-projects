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

        @Test
        public void testFindAll(){
            List<User> users = userMapper.findAll();
            users.forEach(user -> System.out.println(user));
        }

}
