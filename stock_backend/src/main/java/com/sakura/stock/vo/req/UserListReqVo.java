package com.sakura.stock.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author: sakura
 * @date: 2024/3/20 10:20
 * @description: 查询用户列表的请求参数
 */
@ApiModel(description = "查询用户列表的请求参数")
@Data
public class UserListReqVo {
    /**
     * 查询页数
     */
    @ApiModelProperty("查询页数")
    private Integer pageNum = 1;
    /**
     * 每页查询的个数
     */
    @ApiModelProperty("每页查询的个数")
    private Integer pageSize = 20;
    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;
    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickName;
    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private Date startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endTime;
}
