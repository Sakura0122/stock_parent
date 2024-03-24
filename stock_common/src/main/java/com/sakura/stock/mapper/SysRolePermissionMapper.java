package com.sakura.stock.mapper;

import com.sakura.stock.pojo.entity.SysRolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
* @author sakura
* @description 针对表【sys_role_permission(角色权限表)】的数据库操作Mapper
* @createDate 2024-03-10 20:35:10
* @Entity com.sakura.stock.pojo.entity.SysRolePermission
*/
public interface SysRolePermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);

    SysRolePermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRolePermission record);

    int updateByPrimaryKey(SysRolePermission record);

    /**
     * 批量新增角色权限
     * @param sysRolePermissions
     * @return
     */
    int addRolePermissionBatch(@Param("sysRolePermissions") List<SysRolePermission> sysRolePermissions);

    /**
     * 获取角色权限
     * @param roleId 角色id
     * @return
     */
    Set<String> getPermissionIdsByRoleId(@Param("roleId") String roleId);
}
