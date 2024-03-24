package com.sakura.stock.service.impl;

import com.sakura.stock.mapper.SysPermissionMapper;
import com.sakura.stock.pojo.domain.PermissionInfoDomain;
import com.sakura.stock.pojo.entity.SysPermission;
import com.sakura.stock.service.PermissionService;
import com.sakura.stock.utils.IdWorker;
import com.sakura.stock.vo.req.PermissionAddVo;
import com.sakura.stock.vo.resp.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: sakura
 * @date: 2024/3/22 10:58
 * @description:
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    @Resource
    private IdWorker idWorker;

    /**
     * 获取权限信息
     *
     * @return
     */
    @Override
    public R<List<PermissionInfoDomain>> getPermission() {
        // 1.查询所有权限
        List<SysPermission> permissions = sysPermissionMapper.getAllPermission();

        // 2.获取权限树
        List<PermissionInfoDomain> tree = getTree(permissions, "0", false);

        // 3.返回数据
        return R.ok(tree);
    }


    /**
     * 新增权限
     *
     * @param vo
     * @return
     */
    @Override
    public R<String> addPermission(PermissionAddVo vo) {
        // 1.组装数据
        SysPermission sysPermission = new SysPermission();
        BeanUtils.copyProperties(vo, sysPermission);
        sysPermission.setId(idWorker.nextId())
                .setStatus(1).setDeleted(1)
                .setCreateTime(new Date())
                .setUpdateTime(new Date());

        // 2.调用mapper新增
        int count = sysPermissionMapper.insert(sysPermission);

        // 3.判断是否新增成功
        if (count > 0) {
            return R.ok("新增成功");
        } else {
            return R.error("新增失败");
        }
    }

    /**
     * 删除权限
     *
     * @param permissionId 权限id
     * @return
     */
    @Override
    public R<String> deletePermission(String permissionId) {
        // 1.判断当前菜单下是否有子菜单
        int findCount = sysPermissionMapper.findChildrenByParentId(permissionId);
        if (findCount > 0) {
            return R.error(-1, "当前权限存在子权限");
        }

        // 2.调用mapper删除
        int count = sysPermissionMapper.deleteByPrimaryKey(Long.valueOf(permissionId));

        // 3.判断是否删除成功
        if (count == 0) {
            return R.error("删除失败");
        }
        return R.ok("删除成功");
    }

    /**
     * 编辑权限
     *
     * @param vo
     * @return
     */
    @Override
    public R<String> editPermission(PermissionAddVo vo) {
        // 1.组装数据
        SysPermission sysPermission = new SysPermission();
        BeanUtils.copyProperties(vo, sysPermission);
        sysPermission.setUpdateTime(new Date()).setStatus(1).setDeleted(1);

        // 2.调用mapper
        int count = sysPermissionMapper.updateByPrimaryKeySelective(sysPermission);

        // 3.返回数据
        if (count == 0) {
            return R.error("编辑失败");
        }
        return R.ok("编辑成功");
    }

    /**
     * 获取权限树
     *
     * @param permissions 权限集合
     * @param pid         父级id
     * @param isOnlyMenu  菜单还是全部
     * @return
     */
    @Override
    public List<PermissionInfoDomain> getTree(List<SysPermission> permissions, String pid, boolean isOnlyMenu) {
        // 1.创建空集合
        List<PermissionInfoDomain> list = new ArrayList<>();


        // 2.判断permissions是否为空
        if (CollectionUtils.isEmpty(permissions)) {
            return list;
        }

        // 3.递归获取权限树
        for (SysPermission permission : permissions) {
            // 3.1.判断是否为父级
            if (pid.equals(permission.getPid().toString())) {
                if (permission.getType() != 3 || !isOnlyMenu) {
                    PermissionInfoDomain info = new PermissionInfoDomain();
                    BeanUtils.copyProperties(permission, info);
                    info.setChildren(getTree(permissions, permission.getId().toString(), isOnlyMenu));
                    list.add(info);
                }
            }
        }
        return list;
    }
}
