package com.sakura.stock.service;

import com.sakura.stock.pojo.domain.PermissionInfoDomain;
import com.sakura.stock.pojo.entity.SysPermission;
import com.sakura.stock.vo.req.PermissionAddVo;
import com.sakura.stock.vo.resp.R;

import java.util.List;

/**
 * @author: sakura
 * @date: 2024/3/22 10:46
 * @description:
 */
public interface PermissionService {
    /**
     * 获取权限信息
     * @return
     */
    R<List<PermissionInfoDomain>> getPermission();

    /**
     * 获取权限树
     * @param permissions 权限集合
     * @param pid 父级id
     * @param isOnlyMenu 菜单还是全部
     * @return
     */
    List<PermissionInfoDomain> getTree(List<SysPermission> permissions, String pid, boolean isOnlyMenu);

    /**
     * 新增权限
     * @param vo
     * @return
     */
    R<String> addPermission(PermissionAddVo vo);

    /**
     * 删除权限
     * @param permissionId 权限id
     * @return
     */
    R<String> deletePermission(String permissionId);

    /**
     * 编辑权限
     * @param vo
     * @return
     */
    R<String> editPermission(PermissionAddVo vo);
}
