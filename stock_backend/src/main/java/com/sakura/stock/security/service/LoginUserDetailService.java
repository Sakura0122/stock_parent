package com.sakura.stock.security.service;

import com.google.common.base.Strings;
import com.sakura.stock.mapper.SysPermissionMapper;
import com.sakura.stock.mapper.SysRoleMapper;
import com.sakura.stock.mapper.SysRolePermissionMapper;
import com.sakura.stock.mapper.SysUserMapper;
import com.sakura.stock.pojo.domain.PermissionInfoDomain;
import com.sakura.stock.pojo.entity.SysPermission;
import com.sakura.stock.pojo.entity.SysRole;
import com.sakura.stock.pojo.entity.SysUser;
import com.sakura.stock.service.PermissionService;
import com.sakura.stock.security.user.LoginUserDetail;
import com.sakura.stock.vo.resp.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: sakura
 * @date: 2024/4/1 16:50
 * @description: 获取用户详情
 */
@Service
public class LoginUserDetailService implements UserDetailsService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Resource
    private PermissionService permissionService;

    /**
     * 当用户登录认证是，底层会自动调用MyUserDetailService#loadUserByUsername（）把登录的账户名称传入
     * 根据用户名称获取用户的详情信息：用户名 加密密码 权限集合，还包含前端需要的侧边栏树 、前端需要的按钮权限标识的集合等
     *
     * @param loginName
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        // 2.根据用户名查询用户信息
        SysUser dbUser = sysUserMapper.findUserInfoByUserName(loginName);
        // 3.判断查询的用户信息
        if (dbUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        // 4.2 成功则返回用户的正常信息
        // 获取指定用户的权限集合 添加获取侧边栏数据和按钮权限的结合信息
        List<SysPermission> permissions = permissionService.getPermissionByUserId(dbUser.getId());
        List<String> menu = permissionService.getMenu();
        // 前端需要的获取菜单按钮集合
        List<String> authBtnPerms = permissions.stream()
                .filter(per -> !Strings.isNullOrEmpty(per.getCode()) && per.getType() == 3)
                .map(per -> per.getCode()).collect(Collectors.toList());
        // 5.组装后端需要的权限标识
        // 5.1 获取用户拥有的角色
        List<SysRole> roles = sysRoleMapper.getRoleByUserId(dbUser.getId());
        // 5.2 将用户的权限标识和角色标识维护到权限集合中
        List<String> perms = new ArrayList<>();
        permissions.stream().forEach(per -> {
            if (StringUtils.isNotBlank(per.getPerms())) {
                perms.add(per.getPerms());
            }
        });
        roles.stream().forEach(role -> {
            perms.add("ROLE_" + role);
        });
        String[] permStr = perms.toArray(new String[perms.size()]);
        // 5.3 将用户权限标识转化成权限对象集合
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(permStr);
        // 6.封装用户详情信息实体对象
        LoginUserDetail loginUserDetail = new LoginUserDetail();
        // 将用户的id nickname等相同属性信息复制到详情对象中
        BeanUtils.copyProperties(dbUser, loginUserDetail);
        List<String> per = new ArrayList<>();
        loginUserDetail.setMenus(menu);
        loginUserDetail.setAuthorities(authorityList);
        loginUserDetail.setPermissions(authBtnPerms);
        return loginUserDetail;
    }

}
