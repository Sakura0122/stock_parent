package com.sakura.stock.security.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * @author: sakura
 * @date: 2024/4/1 19:29
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginUserDetail implements UserDetails {
    /**
     * 用户名称
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 权限信息
     */
    private List<GrantedAuthority> authorities;

    /**
     * 账户是否过期
     */
    private boolean isAccountNonExpired = true;

    /**
     * 账户是否被锁定
     * true：没有被锁定
     */
    private boolean isAccountNonLocked = true;

    /**
     * 密码是否过期
     * true:没有过期
     */
    private boolean isCredentialsNonExpired = true;

    /**
     * 账户是否禁用
     * true：没有禁用
     */
    private boolean isEnabled = true;

    /**
     * 用户ID
     */
    private String id;
    /**
     * 电话
     */
    private String phone;
    /**
     * 昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 性别(1.男 2.女)
     */
    private Integer sex;

    /**
     * 账户状态(1.正常 2.锁定 )
     */
    private Integer status;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 权限树，不包含按钮相关权限信息
     */
    private List<String> menus;

    /**
     * 按钮权限树
     */
    private List<String> permissions;
}
