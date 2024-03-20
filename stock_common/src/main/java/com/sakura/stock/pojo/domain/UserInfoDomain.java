package com.sakura.stock.pojo.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author: sakura
 * @date: 2024/3/20 10:19
 * @description: 用户信息
 */
@ApiModel(description = "用户信息")
@Data
public class UserInfoDomain {
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private String id;

    /**
     * 账户
     */
    @ApiModelProperty("账户")
    private String username;

    /**
     * 用户密码密文
     */
    @ApiModelProperty("用户密码密文")
    private String password;

    /**
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    private String phone;

    /**
     * 真实名称
     */
    @ApiModelProperty("真实名称")
    private String realName;

    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickName;

    /**
     * 邮箱(唯一)
     */
    @ApiModelProperty("邮箱(唯一)")
    private String email;

    /**
     * 账户状态(1.正常 2.锁定 )
     */
    @ApiModelProperty("账户状态(1.正常 2.锁定 )")
    private Integer status;

    /**
     * 性别(1.男 2.女)
     */
    @ApiModelProperty("性别(1.男 2.女)")
    private Integer sex;

    /**
     * 是否删除(1未删除；0已删除)
     */
    @ApiModelProperty("是否删除(1未删除；0已删除)")
    private Integer deleted;

    /**
     * 创建人id
     */
    @ApiModelProperty("创建人id")
    private String createId;


    /**
     * 创建人姓名
     */
    @ApiModelProperty("创建人姓名")
    private String createUserName;

    /**
     * 更新人id
     */
    @ApiModelProperty("更新人id")
    private String updateId;

    /**
     * 更新人姓名
     */
    @ApiModelProperty("更新人姓名")
    private String updateUserName;

    /**
     * 创建来源(1.web 2.android 3.ios )
     */
    @ApiModelProperty("创建来源(1.web 2.android 3.ios )")
    private Integer createWhere;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;
}
