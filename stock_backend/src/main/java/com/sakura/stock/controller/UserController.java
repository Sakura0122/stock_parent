package com.sakura.stock.controller;

import com.sakura.stock.pojo.entity.SysUser;
import com.sakura.stock.service.UserService;
import com.sakura.stock.vo.req.LoginReqVo;
import com.sakura.stock.vo.resp.LoginRespVo;
import com.sakura.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: sakura
 * @date: 2024/3/10 21:36
 * @description: 定义用户web层接口资源bean
 */
@RestController
@Api(tags = "用户接口")
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
    @ApiOperation(value = "根据用户名称查询用户信息")
    @ApiImplicitParam(name = "username", value = "用户名称", required = true, type = "path")
    @GetMapping("/user/{username}")
    public SysUser getUserByUserName(@PathVariable("username") String name) {
        return userService.findByUsername(name);
    }

    /**
     * 用户登录
     * @param vo
     * @return
     */
    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public R<LoginRespVo> login(@RequestBody LoginReqVo vo) {
        return userService.login(vo);
    }

    /**
     * 生成图片验证码
     * @return
     */
    @GetMapping("/captcha")
    public R<Map> getCaptchaCode() {
        return userService.getCaptchaCode();
    }
}
