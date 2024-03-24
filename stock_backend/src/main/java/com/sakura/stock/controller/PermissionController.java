package com.sakura.stock.controller;

import com.sakura.stock.pojo.domain.PermissionInfoDomain;
import com.sakura.stock.service.PermissionService;
import com.sakura.stock.vo.req.PermissionAddVo;
import com.sakura.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: sakura
 * @date: 2024/3/22 10:45
 * @description: 权限接口
 */
@Api(tags = "权限接口")
@RestController
@RequestMapping("/api")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    /**
     * 获取权限
     *
     * @return
     */
    @ApiOperation(value = "获取权限", notes = "获取权限", httpMethod = "GET")
    @GetMapping("/permission")
    public R<List<PermissionInfoDomain>> permission() {
        return permissionService.getPermission();
    }

    /**
     * 新增权限
     *
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "PermissionAddVo", name = "vo", value = "", required = true)
    })
    @ApiOperation(value = "新增权限", notes = "新增权限", httpMethod = "POST")
    @PostMapping("/permission")
    public R<String> addPermission(@RequestBody PermissionAddVo vo) {
        if (vo == null || vo.getType() == null || vo.getPid() == null) {
            return R.error("参数不能为空");
        }
        return permissionService.addPermission(vo);
    }

    /**
     * 删除权限
     * @param permissionId 权限id
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "string", name = "permissionId", value = "权限id", required = true)
    })
    @ApiOperation(value = "删除权限", notes = "删除权限", httpMethod = "DELETE")
    @DeleteMapping("/permission/{permissionId}")
    public R<String> deletePermission(@PathVariable("permissionId") String permissionId) {
        if (StringUtils.isBlank(permissionId)) {
            return R.error("参数不能为空");
        }
        return permissionService.deletePermission(permissionId);
    }

    /**
     * 编辑权限
     * @param vo
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "PermissionAddVo", name = "vo", value = "", required = true)
    })
    @ApiOperation(value = "编辑权限", notes = "编辑权限", httpMethod = "PUT")
    @PutMapping("/permission")
    public R<String> editPermission(@RequestBody PermissionAddVo vo){
        if (vo == null || vo.getType() == null || vo.getPid() == null) {
            return R.error("参数不能为空");
        }
        return permissionService.editPermission(vo);
    }
}
