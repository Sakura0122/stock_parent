package com.sakura.stock.service;

import com.sakura.stock.pojo.entity.SysRole;
import com.sakura.stock.vo.req.RoleAddVo;
import com.sakura.stock.vo.resp.R;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author: sakura
 * @date: 2024/3/21 17:42
 * @description:
 */
public interface RoleService {
    /**
     * 获取所有角色
     * @return
     */
    R<List<SysRole>> getAllRoles();

    /**
     * 新增角色
     * @param vo
     * @return
     */
    R<String> addRoleWithPermissions(RoleAddVo vo);

    /**
     * 删除角色
     * @param id 角色id
     * @return
     */
    R<String> deleteRoleById(String id);

    /**
     * 获取角色权限
     * @param roleId 角色id
     * @return
     */
    R<Set<String>> getPermissionIdsByRoleId(String roleId);
}
