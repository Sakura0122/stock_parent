package com.sakura.stock.controller;

import com.sakura.stock.pojo.domain.UserInfoDomain;
import com.sakura.stock.pojo.entity.SysUser;
import com.sakura.stock.service.UserService;
import com.sakura.stock.vo.req.LoginReqVo;
import com.sakura.stock.vo.req.UserAddReqVo;
import com.sakura.stock.vo.req.UserEditReqVO;
import com.sakura.stock.vo.req.UserListReqVo;
import com.sakura.stock.vo.resp.LoginRespVo;
import com.sakura.stock.vo.resp.PageResult;
import com.sakura.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
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

    @Resource
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
     *
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
     *
     * @return
     */
    @GetMapping("/captcha")
    public R<Map> getCaptchaCode() {
        return userService.getCaptchaCode();
    }

    /**
     * 多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围
     *
     * @param vo
     * @return
     */
    @ApiOperation(value = "多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围", notes = "多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围", httpMethod = "GET")
    @PostMapping("/users")
    public R<PageResult<UserInfoDomain>> getUserList(@RequestBody(required = false) UserListReqVo vo) {
        if (vo == null) {
            vo = new UserListReqVo();
        }
        return R.ok(userService.getUserList(vo));
    }

    /**
     * 添加用户信息
     *
     * @param vo
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserAddReqVo", name = "vo", value = "", required = true)
    })
    @ApiOperation(value = "添加用户信息", notes = "添加用户信息", httpMethod = "POST")
    @PostMapping("/user")
    public R<String> addUser(@RequestBody UserAddReqVo vo) {
        if (vo == null) {
            return R.error("参数不能为空");
        }
        return userService.addUser(vo);
    }

    /**
     * 更新用户信息
     *
     * @param vo
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserAddReqVo", name = "vo", value = "", required = true)
    })
    @ApiOperation(value = "更新用户信息", notes = "更新用户信息", httpMethod = "PUT")
    @PutMapping("/user")
    public R<String> updateUser(@RequestBody UserEditReqVO vo) {
        if (vo == null) {
            return R.error("参数不能为空");
        }
        return userService.updateUser(vo);
    }

    /**
     * 批量删除用户
     * @param userIds 用户id集合
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "List<String>", name = "userIds", value = "用户id集合", required = true)
    })
    @ApiOperation(value = "批量删除用户", notes = "批量删除用户", httpMethod = "DELETE")
    @DeleteMapping("/user")
    public R<String> deleteUsers(@RequestBody List<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return R.error("参数不能为空");
        }
        return userService.deleteUsers(userIds);
    }
}
