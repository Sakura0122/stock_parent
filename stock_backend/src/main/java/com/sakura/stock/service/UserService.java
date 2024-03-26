package com.sakura.stock.service;

import com.sakura.stock.pojo.domain.UserInfoDomain;
import com.sakura.stock.pojo.entity.SysUser;
import com.sakura.stock.vo.req.LoginReqVo;
import com.sakura.stock.vo.req.UserAddReqVo;
import com.sakura.stock.vo.req.UserEditReqVO;
import com.sakura.stock.vo.req.UserListReqVo;
import com.sakura.stock.vo.resp.LoginRespVo;
import com.sakura.stock.vo.resp.PageResult;
import com.sakura.stock.vo.resp.R;

import java.util.List;
import java.util.Map;

/**
 * @author: sakura
 * @date: 2024/3/10 21:27
 * @description: 定义用户服务接口
 */
public interface UserService {
    /**
     * 根据用户名称查询用户信息
     * @param username
     * @return
     */
    SysUser findByUsername(String username);

    /**
     * 用户登录功能
     * @param vo
     * @return
     */
    R<LoginRespVo> login(LoginReqVo vo);

    /**
     * 生成图片验证码
     * @return
     */
    R<Map> getCaptchaCode();

    /**
     * 多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围
     * @param vo
     * @return
     */
    PageResult<UserInfoDomain> getUserList(UserListReqVo vo);

    /**
     * 添加用户信息
     *
     * @param vo
     * @return
     */
    R<String> addUser(UserAddReqVo vo);

    /**
     * 更新用户信息
     * @param vo
     * @return
     */
    R<String> updateUser(UserEditReqVO vo);

    /**
     * 批量删除用户
     * @param userIds 用户id集合
     * @return
     */
    R<String> deleteUsers(List<String> userIds);

    /**
     * 获取用户信息
     * @param id 用户id
     * @return
     */
    R<UserInfoDomain> getUserById(String id);

    /**
     * 获取所有角色信息和当前用户的角色信息
     * @param userId
     * @return
     */
    R<Map<String, List>> getUserRoleInfo(String userId);

    /**
     * 编辑用户角色信息
     * @param userId 用户id
     * @param roleIds 角色id列表
     * @return
     */
    R<String> editUserRoleInfo(String userId, List<String> roleIds);
}
