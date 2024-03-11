package com.sakura.stock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author: sakura
 * @date: 2024/3/11 23:06
 * @description: 测试密码加密
 */
@SpringBootTest
public class TestPasswordEncoder {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 测试密码加密
     */
    @Test
    public void testPasswordEncoder(){
        String encode = passwordEncoder.encode("123456");
        System.out.println(encode); // $2a$10$WnS1FWPCS8CowSCmsohJ4..OV.5VcUDOejWmn.6Neob.v5WemV9oO
    }

    /**
     * 测试密码匹配
     */
    @Test
    public void testPasswordMatch(){
        String pwd = "123456";
        String enPwd = "$2a$10$WnS1FWPCS8CowSCmsohJ4..OV.5VcUDOejWmn.6Neob.v5WemV9oO";
        System.out.println(passwordEncoder.matches(pwd, enPwd));
    }
}
