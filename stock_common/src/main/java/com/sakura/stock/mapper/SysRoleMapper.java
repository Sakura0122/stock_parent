package com.sakura.stock.mapper;

import com.sakura.stock.pojo.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author sakura
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2024-03-10 20:35:10
* @Entity com.sakura.stock.pojo.entity.SysRole
*/
public interface SysRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    /**
     * 获取所有角色
     * @return
     */
    List<SysRole> getAllRoles();

    /**
     * 删除角色
     * @param id 角色id
     * @return
     */
    int deleteRoleById(@Param("id") Long id);

    /**
     * 根据用户id查询角色信息
     * @param userId
     * @return
     */
    List<SysRole> getRoleByUserId(@Param("userId") Long userId);
}
