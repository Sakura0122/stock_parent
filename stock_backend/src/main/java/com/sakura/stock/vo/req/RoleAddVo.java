package com.sakura.stock.vo.req;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: sakura
 * @date: 2024/3/23 15:28
 * @description: 角色添加VO
 */
@ApiModel(description = "角色添加VO")
@Data
public class RoleAddVo {

    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    private String name;
    /**
     * 角色描述
     */
    @ApiModelProperty("角色描述")
    private String description;

    /**
     * 权限ID集合
     */
    @ApiModelProperty("权限ID集合")
    private List<Long> permissionsIds;
}
