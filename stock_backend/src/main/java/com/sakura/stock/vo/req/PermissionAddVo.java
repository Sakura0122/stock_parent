package com.sakura.stock.vo.req;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.json.JSONString;

/**
 * @author: sakura
 * @date: 2024/3/22 16:33
 * @description: 新增权限vo
 */
@ApiModel(description = "新增权限vo")
@Data
public class PermissionAddVo {
    @ApiModelProperty("主键id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 菜单等级 0 顶级目录 1.目录 2 菜单 3 按钮
     */
    @ApiModelProperty("菜单等级 0 顶级目录 1.目录 2 菜单 3 按钮")
    private Integer type;

    /**
     * 菜单标题
     */
    @ApiModelProperty("菜单标题")
    private String title;

    /**
     * 权限标识
     */
    @ApiModelProperty("权限标识")
    private String code;

    /**
     * 菜单父级id
     */
    @ApiModelProperty("菜单父级id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long pid;

    /**
     * 只有菜单类型有名称，默认是路由的名称
     */
    @ApiModelProperty("只有菜单类型有名称，默认是路由的名称")
    private String name;

    /**
     * 1.基于springSecrutiry约定的权限过滤便是
     */
    // @ApiModelProperty("1.基于springSecrutiry约定的权限过滤便是")
    // private String perms;
}
