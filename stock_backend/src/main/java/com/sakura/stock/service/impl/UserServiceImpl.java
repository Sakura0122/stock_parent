package com.sakura.stock.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sakura.stock.constant.StockConstant;
import com.sakura.stock.mapper.SysUserMapper;
import com.sakura.stock.pojo.domain.UserInfoDomain;
import com.sakura.stock.pojo.entity.SysUser;
import com.sakura.stock.service.UserService;
import com.sakura.stock.utils.IdWorker;
import com.sakura.stock.vo.req.LoginReqVo;
import com.sakura.stock.vo.req.UserAddReqVo;
import com.sakura.stock.vo.req.UserEditReqVO;
import com.sakura.stock.vo.req.UserListReqVo;
import com.sakura.stock.vo.resp.LoginRespVo;
import com.sakura.stock.vo.resp.PageResult;
import com.sakura.stock.vo.resp.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: sakura
 * @date: 2024/3/10 21:29
 * @description: 定义用户服务实现
 */
@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private IdWorker idWorker;

    @Resource
    private RedisTemplate redisTemplate;

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
        if (vo == null || StringUtils.isBlank(vo.getUsername()) || StringUtils.isBlank(vo.getPassword())) {
            return R.error("参数不能为空");
        }
        // 验证码是否存在
        if (StringUtils.isBlank(vo.getCode()) || StringUtils.isBlank(vo.getSessionId())) {
            return R.error("验证码错误");
        }
        // 判断redis保存的验证码与输入的验证码是否相同（忽略大小写）
        String redisCode = (String) redisTemplate.opsForValue().get(StockConstant.CHECK_PREFIX + vo.getSessionId());
        if (StringUtils.isBlank(redisCode)) {
            return R.error("验证码已过期");
        }
        if (!redisCode.equalsIgnoreCase(vo.getCode())) {
            return R.error("验证码错误");
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

    /**
     * 生成图片验证码
     *
     * @return
     */
    @Override
    public R<Map> getCaptchaCode() {
        // 1.生成图片验证码
        // 参数分别是宽、高、验证码长度、干扰线数量
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(250, 40, 4, 5);
        // 设置背景颜色
        lineCaptcha.setBackground(Color.lightGray);
        // 自定义生成校验码规则
        // lineCaptcha.setGenerator(new CodeGenerator() {
        //     @Override
        //     public String generate() {
        //         // 自定义校验码生成逻辑
        //         return null;
        //     }
        //
        //     @Override
        //     public boolean verify(String s, String s1) {
        //         // 匹配校验码逻辑
        //         return false;
        //     }
        // });


        // 获取校验码
        String code = lineCaptcha.getCode();
        // 获取base64图片
        String imageBase64 = lineCaptcha.getImageBase64();

        // 2.生成sessionId
        String sessionId = String.valueOf(idWorker.nextId());

        log.info("当前生成的图片校验码：{}，会话id：{}", code, sessionId);

        // 3.将sessionId作为key 校验码作为value 存入redis
        redisTemplate.opsForValue().set(StockConstant.CHECK_PREFIX + sessionId, code, 60, TimeUnit.SECONDS);

        // 4.组装对象
        Map<String, String> data = new HashMap<>();
        data.put("imageData", imageBase64);
        data.put("sessionId", sessionId);

        // 5.响应数据
        return R.ok(data);
    }

    /**
     * 多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围
     *
     * @param vo
     * @return
     */
    @Override
    public PageResult<UserInfoDomain> getUserList(UserListReqVo vo) {
        // 1.设置分页参数
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());

        // 2.调用mapper查询
        List<UserInfoDomain> list = sysUserMapper.getUserList(vo.getUsername(), vo.getNickName(), vo.getStartTime(), vo.getEndTime());

        // 3.组装pageResult对象
        PageInfo<UserInfoDomain> pageInfo = new PageInfo<>(list);
        PageResult<UserInfoDomain> data = new PageResult<>(pageInfo);

        // 4.响应数据
        return data;
    }

    /**
     * 添加用户信息
     *
     * @param vo
     * @return
     */
    @Override
    public R<String> addUser(UserAddReqVo vo) {
        // 1.判断用户是否存在
        SysUser userInfoByUserName = sysUserMapper.findUserInfoByUserName(vo.getUsername());
        if (userInfoByUserName != null) {
            return R.error("用户名已存在");
        }

        // 2.封装用户信息
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(vo, sysUser);
        // 2.1 设置id
        sysUser.setId((idWorker.nextId()));
        // 2.2 密码加密
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        // 2.3 设置添加时间和更新时间
        sysUser.setCreateTime(new Date());
        sysUser.setUpdateTime(new Date());
        // 2.4 是否删除
        sysUser.setDeleted(1);
        // TODO 获取当前操作用户的id

        // 3.添加用户
        int count = sysUserMapper.insert(sysUser);
        if (count != 1) {
            return R.error("添加用户失败");
        }
        return R.ok("添加用户成功");
    }

    /**
     * 更新用户信息
     *
     * @param vo
     * @return
     */
    @Override
    public R<String> updateUser(UserEditReqVO vo) {
        // 1.获取用户信息
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(vo, sysUser);
        // TODO 设置更新者ID

        // 2.1 设置更新时间
        sysUser.setUpdateTime(new Date());
        // 2.2 设置id
        sysUser.setId(Long.parseLong(vo.getId()));

        // 3.更新用户信息
        int count = sysUserMapper.updateByPrimaryKeySelective(sysUser);
        if (count != 1) {
            return R.error("更新用户失败");
        }

        // 4.响应数据
        return R.ok("更新用户成功");
    }

    /**
     * 批量删除用户
     *
     * @param userIds 用户id集合
     * @return
     */
    @Override
    public R<String> deleteUsers(List<String> userIds) {
        // 1.调用mapper批量删除（设置deleted为0）
        int count = sysUserMapper.deleteUsers(userIds);
        if (count != 0) {
            return R.error("删除用户失败");
        }
        return R.ok("删除用户成功");
    }

    /**
     * 获取用户信息
     *
     * @param id 用户id
     * @return
     */
    @Override
    public R<UserInfoDomain> getUserById(String id) {
        // 1.调用mapper查询
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(Long.parseLong(id));
        if (sysUser == null) {
            return R.error("用户不存在");
        }

        // 2.组装对象
        UserInfoDomain userInfoDomain = new UserInfoDomain();
        BeanUtils.copyProperties(sysUser, userInfoDomain);
        userInfoDomain.setId(String.valueOf(sysUser.getId()));

        // 3.响应数据
        return R.ok(userInfoDomain);
    }
}
