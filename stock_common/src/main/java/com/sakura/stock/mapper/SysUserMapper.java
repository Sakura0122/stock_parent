package com.sakura.stock.mapper;

import com.sakura.stock.pojo.domain.UserInfoDomain;
import com.sakura.stock.pojo.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author sakura
 * @description 针对表【sys_user(用户表)】的数据库操作Mapper
 * @createDate 2024-03-10 20:35:10
 * @Entity com.sakura.stock.pojo.entity.SysUser
 */
public interface SysUserMapper {

    int deleteByPrimaryKey(String id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(String id);

    /**
     * 更新用户信息
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    /**
     * 查询所有用户
     *
     * @return
     */
    List<SysUser> selectAllUser();

    SysUser findUserInfoByUserName(String username);

    /**
     * 查询用户列表
     *
     * @param username  用户名
     * @param nickName  昵称
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    List<UserInfoDomain> getUserList(@Param("username") String username, @Param("nickName") String nickName, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 批量删除用户
     * @param userIds 用户id集合
     * @return
     */
    int deleteUsers(@Param("userIds") List<String> userIds);
}
