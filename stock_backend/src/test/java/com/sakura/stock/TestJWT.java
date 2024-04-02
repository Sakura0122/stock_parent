package com.sakura.stock;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import javassist.expr.Instanceof;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * @author: sakura
 * @date: 2024/3/28 14:49
 * @description:
 */
public class TestJWT {

    @Test
    public void test01() {
        DateTime now = DateTime.now();
        System.out.println(now.toString("yyyy/MM/dd"));
        // DateTime dateTime = now.offsetNew(DateField.MINUTE, 1);
        // String token = JWT.create()
        //         .setPayload("id", "123456")
        //         .setKey("sakura".getBytes())
        //         .sign();
        // System.out.println(token);
    }

    @Test
    public void test02() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEyMzQ1NiIsImlhdCI6MTcxMTYxMDA1NiwiZXhwIjoxNzExNjEwMTE2LCJuYmYiOjE3MTE2MTAwNTZ9.0ykw3MMWnk4zWG2UPS5XhFXPs-F98ClwZLuJGK7RzTc\n";
        JWT jwt = JWT.of(token);
        Object id = jwt.getPayload("id");
        System.out.println(id);
        System.out.println(JWT.of(token).setKey("sakura".getBytes()).verify());
        System.out.println(JWTUtil.parseToken(token).getPayloads());
        System.out.println(JWTUtil.parseToken(token).validate(0));
    }
}
