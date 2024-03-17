package com.sakura.stock;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sakura.stock.mapper.SysUserMapper;
import com.sakura.stock.pojo.entity.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author: sakura
 * @date: 2024/3/16 13:45
 * @description:
 */
@SpringBootTest
public class TestPageHelper {

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 测试分页
     */
    @Test
    public void test01() {
        // 当前页
        int page = 2;
        // 每页显示多少条
        int pageSize = 5;

        PageHelper.startPage(page, pageSize);

        List<SysUser> sysUsers = sysUserMapper.selectAllUser();

        PageInfo<SysUser> sysUserPageInfo = new PageInfo<>(sysUsers);

        // 当前页
        int pageNum = sysUserPageInfo.getPageNum();
        // 总页数
        int pages = sysUserPageInfo.getPages();
        // 每页显示多少条
        int pageSize1 = sysUserPageInfo.getPageSize();
        // 当前页显示多少条
        int size = sysUserPageInfo.getSize();
        // 总条数
        long total = sysUserPageInfo.getTotal();
        List<SysUser> list = sysUserPageInfo.getList();
        System.out.println(pageNum);
        System.out.println(pages);
        System.out.println(pageSize1);
        System.out.println(size);
        System.out.println(total);
        System.out.println(list);
    }
}
