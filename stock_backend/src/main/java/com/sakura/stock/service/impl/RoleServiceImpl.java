package com.sakura.stock.service.impl;

import com.sakura.stock.mapper.SysRoleMapper;
import com.sakura.stock.mapper.SysRolePermissionMapper;
import com.sakura.stock.pojo.entity.SysRole;
import com.sakura.stock.pojo.entity.SysRolePermission;
import com.sakura.stock.service.RoleService;
import com.sakura.stock.utils.IdWorker;
import com.sakura.stock.vo.req.RoleAddVo;
import com.sakura.stock.vo.resp.R;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: sakura
 * @date: 2024/3/21 17:42
 * @description:
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Resource
    private IdWorker idWorker;

    /**
     * 获取所有角色
     *
     * @return
     */
    @Override
    public R<List<SysRole>> getAllRoles() {
        // 1.调用mapper获取数据
        List<SysRole> data = sysRoleMapper.getAllRoles();
        return R.ok(data);
    }

    /**
     * 获取角色权限
     *
     * @param roleId 角色id
     * @return
     */
    @Override
    public R<Set<String>> getPermissionIdsByRoleId(String roleId) {
        // 1.调用mapper查询权限
        Set<String> permissionIds = sysRolePermissionMapper.getPermissionIdsByRoleId(roleId);
        permissionIds = permissionIds == null ? new HashSet<>() : permissionIds;

        // 2.返回数据
        return R.ok(permissionIds);
    }

    /**
     * 新增角色
     *
     * @param vo
     * @return
     */
    @Override
    public R<String> addRoleWithPermissions(RoleAddVo vo) {
        // 1.组装数据
        long roleId = idWorker.nextId();
        SysRole role = SysRole.builder()
                .id(roleId).name(vo.getName()).description(vo.getDescription())
                .createTime(new Date()).updateTime(new Date()).status(1).deleted(1).build();

        // 2.调用mapper新增角色
        int roleCount = sysRoleMapper.insert(role);
        if (roleCount != 1) {
            return R.error("新增角色失败");
        }

        // 3.批量添加角色的权限
        List<Long> permissionsIds = vo.getPermissionsIds();
        if (!CollectionUtils.isEmpty(permissionsIds)) {
            List<SysRolePermission> sysRolePermissions = permissionsIds.stream().map(permissionId -> {
                return SysRolePermission.builder().id(idWorker.nextId())
                        .roleId(roleId).permissionId(permissionId).createTime(new Date()).build();
            }).collect(Collectors.toList());
            int count = sysRolePermissionMapper.addRolePermissionBatch(sysRolePermissions);
            if (count == 0) {
                return R.ok("给角色设置权限失败");
            }
        }
        return R.ok("新增成功");
    }

    /**
     * 编辑角色
     *
     * @param vo
     * @return
     */
    @Override
    public R<String> editRoleWithPermissions(RoleAddVo vo) {
        // 1.组装数据
        SysRole sysRole = SysRole.builder().id(vo.getId()).name(vo.getName()).description(vo.getDescription())
                .status(1).deleted(1).updateTime(new Date()).build();

        // 2.调用mapper更新数据
        int updateCount = sysRoleMapper.updateByPrimaryKeySelective(sysRole);
        if (updateCount != 1) {
            return R.error("更新失败");
        }

        // 3.删除当前角色关联的权限信息
        sysRolePermissionMapper.deleteByRoleId(String.valueOf(vo.getId()));

        // 4.批量添加角色权限
        List<Long> permissionsIds = vo.getPermissionsIds();
        if (!CollectionUtils.isEmpty(permissionsIds)) {
            List<SysRolePermission> sysRolePermissions = permissionsIds.stream().map(permissionId -> {
                SysRolePermission sysRolePermission = SysRolePermission.builder().id(idWorker.nextId())
                        .roleId(vo.getId()).permissionId(permissionId).createTime(new Date()).build();
                return sysRolePermission;
            }).collect(Collectors.toList());
            int count = sysRolePermissionMapper.addRolePermissionBatch(sysRolePermissions);
            if (count == 0) {
                return R.ok("给角色设置权限失败");
            }
        }
        return R.ok("新增成功");
    }

    /**
     * 删除角色
     *
     * @param id 角色id
     * @return
     */
    @Override
    public R<String> deleteRoleById(String id) {
        // 1.调用mapper删除角色
        int count = sysRoleMapper.deleteRoleById(Long.valueOf(id));
        if (count == 0) {
            return R.error("删除失败");
        }
        return R.ok("删除成功");
    }
}
