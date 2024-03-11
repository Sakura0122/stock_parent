package com.sakura.stock.service.impl;

import com.sakura.stock.mapper.SysUserMapper;
import com.sakura.stock.pojo.entity.SysUser;
import com.sakura.stock.service.UserService;
import com.sakura.stock.vo.req.LoginReqVo;
import com.sakura.stock.vo.resp.LoginRespVo;
import com.sakura.stock.vo.resp.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author: sakura
 * @date: 2024/3/10 21:29
 * @description: 定义用户服务实现
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return
     */
    @Override
    public SysUser findByUsername(String username) {
        return sysUserMapper.findUserInfoByUserName(username);
    }

    /**
     * 用户登录功能
     *
     * @param vo
     * @return
     */
    @Override
    public R<LoginRespVo> login(LoginReqVo vo) {
        // 1.判断参数是否合法
        if (vo == null || StringUtils.isBlank(vo.getUsername()) || StringUtils.isBlank(vo.getPassword()) || StringUtils.isBlank(vo.getCode())) {
            return R.error("参数不能为空");
        }

        // 2.根据用户名去数据库中查询用户信息 获取密码的密文
        SysUser dbUser = sysUserMapper.findUserInfoByUserName(vo.getUsername());
        if (dbUser == null) {
            return R.error("用户名不存在");
        }

        // 3.调用密码匹配器进行密码匹配
        if (!passwordEncoder.matches(vo.getPassword(), dbUser.getPassword())) {
            return R.error("密码错误");
        }

        // 4.响应
        // LoginRespVo respVo = new LoginRespVo(dbUser.getId(), dbUser.getPhone(), dbUser.getUsername(), dbUser.getNickName());
        LoginRespVo respVo = new LoginRespVo();
        // 必须保障属性名称和类型一致
        BeanUtils.copyProperties(dbUser, respVo);
        return R.ok(respVo);
    }
}
