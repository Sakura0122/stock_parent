package com.sakura.stock.controller;

import com.sakura.stock.pojo.entity.SysRole;
import com.sakura.stock.service.RoleService;
import com.sakura.stock.vo.req.RoleAddVo;
import com.sakura.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author: sakura
 * @date: 2024/3/21 17:40
 * @description: 角色接口
 */
@Api(tags = "角色接口")
@RestController
@RequestMapping("/api")
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * 获取所有角色
     *
     * @return
     */
    @ApiOperation(value = "获取所有角色", notes = "获取所有角色", httpMethod = "POST")
    @GetMapping("/role")
    public R<List<SysRole>> getAllRoles() {
        return roleService.getAllRoles();
    }

    /**
     * 获取角色的权限id
     *
     * @param roleId
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "string", name = "roleId", value = "", required = true)
    })
    @ApiOperation(value = "获取角色的权限id", notes = "获取角色的权限id", httpMethod = "GET")
    @GetMapping("/role/{roleId}")
    public R<Set<String>> getPermissionIdsByRoleId(@PathVariable("roleId") String roleId) {
        if (StringUtils.isBlank(roleId)) {
            return R.error("参数不能为空");
        }
        return roleService.getPermissionIdsByRoleId(roleId);
    }

    /**
     * 新增角色
     *
     * @param vo
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "RoleAddVo", name = "vo", value = "", required = true)
    })
    @ApiOperation(value = "新增角色", notes = "新增角色", httpMethod = "POST")
    @PostMapping("/role")
    public R<String> addRoleWithPermissions(@RequestBody RoleAddVo vo) {
        if (vo == null || StringUtils.isBlank(vo.getName())) {
            return R.error("参数不能为空");
        }
        return roleService.addRoleWithPermissions(vo);
    }

    /**
     * 编辑角色
     * @param vo
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "RoleAddVo", name = "vo", value = "", required = true)
    })
    @ApiOperation(value = "编辑角色", notes = "编辑角色", httpMethod = "PUT")
    @PutMapping("/role")
    public R<String> editRoleWithPermission(@RequestBody RoleAddVo vo) {
        if (vo == null || StringUtils.isBlank(vo.getName()) || vo.getId() == null) {
            return R.error("参数不能为空");
        }
        return roleService.editRoleWithPermissions(vo);
    }

    /**
     * 删除角色
     *
     * @param id 角色id
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "string", name = "id", value = "角色id", required = true)
    })
    @ApiOperation(value = "删除角色", notes = "删除角色", httpMethod = "DELETE")
    @DeleteMapping("/role/{roleId}")
    public R<String> deleteRoleById(@PathVariable("roleId") String id) {
        if (StringUtils.isBlank(id)) {
            return R.error("参数不能为空");
        }
        return roleService.deleteRoleById(id);
    }
}
