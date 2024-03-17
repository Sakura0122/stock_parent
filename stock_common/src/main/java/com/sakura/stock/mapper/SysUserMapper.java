package com.sakura.stock.mapper;

import com.sakura.stock.pojo.entity.SysUser;

import java.util.List;

/**
 * @author sakura
 * @description 针对表【sys_user(用户表)】的数据库操作Mapper
 * @createDate 2024-03-10 20:35:10
 * @Entity com.sakura.stock.pojo.entity.SysUser
 */
public interface SysUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    /**
     * 查询所有用户
     * @return
     */
    List<SysUser> selectAllUser();

    SysUser findUserInfoByUserName(String username);
}
