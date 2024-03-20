package com.sakura.stock.vo.req;

import lombok.Data;

/**
 * @author: sakura
 * @date: 2024/3/20 13:48
 * @description: 添加用户vo
 */
@Data
public class UserAddReqVo {
    /**
     * 账户名称
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 性别 1：男 0：女
     */
    private Integer sex;
    /**
     * 账户装填 1.正常 2. 锁定
     */
    private Integer status;
    /**
     * 创建来源 1.web 2.android 3.ios
     */
    private Integer createWhere;
}
