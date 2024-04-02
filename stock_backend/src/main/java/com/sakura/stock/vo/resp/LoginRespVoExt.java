package com.sakura.stock.vo.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: sakura
 * @date: 2024/4/1 19:37
 * @description:
 */
@Data
public class LoginRespVoExt {
    /**
     * 用户ID
     * 将Long类型数字进行json格式转化时，转成String格式类型
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    private String phone;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 性别(1.男 2.女)
     */
    private Integer sex;

    /**
     * 账户状态(1.正常 2.锁定 )
     */
    private Integer status;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 权限树，不包含按钮相关权限信息
     */
    private List<String> menus;

    /**
     * 按钮权限树
     */
    private List<String> permissions;

    /*
        响应前端token
     */
    private String accessToken;
}
