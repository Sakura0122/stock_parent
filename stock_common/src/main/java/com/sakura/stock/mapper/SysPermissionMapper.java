package com.sakura.stock.mapper;

import com.sakura.stock.pojo.entity.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author sakura
* @description 针对表【sys_permission(权限表（菜单）)】的数据库操作Mapper
* @createDate 2024-03-10 20:35:10
* @Entity com.sakura.stock.pojo.entity.SysPermission
*/
public interface SysPermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

    /**
     * 获取所有权限
     * @return
     */
    List<SysPermission> getAllPermission();

    /**
     * 查询当前菜单下是否有子菜单
     * @param permissionId 权限id
     * @return
     */
    int findChildrenByParentId(@Param("permissionId") String permissionId);

    /**
     * 根据用户id查询用户信息
     * @param id
     * @return
     */
    List<SysPermission> getPermissionByUserId(@Param("userId") long id);

    /**
     * 获取所有菜单
     * @return
     */
    List<String> getMenu();
}
