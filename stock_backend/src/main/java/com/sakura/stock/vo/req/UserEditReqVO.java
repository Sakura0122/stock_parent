package com.sakura.stock.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: sakura
 * @date: 2024/3/20 14:49
 * @description: 更新用户基本信息vo
 */
@ApiModel(description = "更新用户基本信息vo")
@Data
public class UserEditReqVO {
    /**
     * 账户id
     */
    @ApiModelProperty("账户id")
    private String id;
    /**
     * 账户名称
     */
    @ApiModelProperty("账户名称")
    private String username;
    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;
    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;
    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickName;
    /**
     * 真实姓名
     */
    @ApiModelProperty("真实姓名")
    private String realName;
    /**
     * 性别 1：男 0：女
     */
    @ApiModelProperty("性别 1：男 0：女")
    private Integer sex;
    /**
     * 账户装填 1.正常 2. 锁定
     */
    @ApiModelProperty("账户装填 1.正常 2. 锁定")
    private Integer status;
    /**
     * 创建来源 1.web 2.android 3.ios
     */
    @ApiModelProperty("创建来源 1.web 2.android 3.ios")
    private Integer createWhere;
}
