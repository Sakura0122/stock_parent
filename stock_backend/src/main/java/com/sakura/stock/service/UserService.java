package com.sakura.stock.service;

import com.sakura.stock.pojo.entity.SysUser;
import com.sakura.stock.vo.req.LoginReqVo;
import com.sakura.stock.vo.resp.LoginRespVo;
import com.sakura.stock.vo.resp.R;

import java.util.Map;

/**
 * @author: sakura
 * @date: 2024/3/10 21:27
 * @description: 定义用户服务接口
 */
public interface UserService {
    /**
     * 根据用户名称查询用户信息
     * @param username
     * @return
     */
    SysUser findByUsername(String username);

    /**
     * 用户登录功能
     * @param vo
     * @return
     */
    R<LoginRespVo> login(LoginReqVo vo);

    /**
     * 生成图片验证码
     * @return
     */
    R<Map> getCaptchaCode();
}
