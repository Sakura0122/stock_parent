package com.sakura.stock.vo.req;

import lombok.Data;

/**
 * @author: sakura
 * @date: 2021/12/30
 * @description: 登录请求vo
 */
@Data
public class LoginReqVo {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 验证码
     */
    private String code;
}
