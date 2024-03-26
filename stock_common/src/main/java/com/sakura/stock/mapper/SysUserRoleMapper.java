package com.sakura.stock.mapper;

import com.sakura.stock.pojo.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author sakura
* @description 针对表【sys_user_role(用户角色表)】的数据库操作Mapper
* @createDate 2024-03-10 20:35:10
* @Entity com.sakura.stock.pojo.entity.SysUserRole
*/
public interface SysUserRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    SysUserRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);

    /**
     * 获取用户所有的角色id
     * @param userId
     * @return
     */
    List<String> getUserRoleId(@Param("userId") String userId);

    /**
     * 删除用户的角色
     * @param userId
     * @return
     */
    int deleteByUserId(@Param("userId") String userId);

    /**
     * 批量插入用户角色
     * @param list
     * @return
     */
    int insertBatch(@Param("userRoles") List<SysUserRole> list);
}
