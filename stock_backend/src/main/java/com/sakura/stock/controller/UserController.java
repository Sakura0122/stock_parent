package com.sakura.stock.controller;

import com.sakura.stock.pojo.entity.SysUser;
import com.sakura.stock.service.UserService;
import com.sakura.stock.vo.req.LoginReqVo;
import com.sakura.stock.vo.resp.LoginRespVo;
import com.sakura.stock.vo.resp.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: sakura
 * @date: 2024/3/10 21:36
 * @description: 定义用户web层接口资源bean
 */
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 根据用户名称查询用户信息
     *
     * @param name
     * @return
     */
    @GetMapping("/user/{username}")
    public SysUser getUserByUserName(@PathVariable("username") String name) {
        return userService.findByUsername(name);
    }

    /**
     * 用户登录
     * @param vo
     * @return
     */
    @PostMapping("/login")
    public R<LoginRespVo> login(@RequestBody LoginReqVo vo) {
        return userService.login(vo);
    }
}
