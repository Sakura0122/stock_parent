package com.sakura.stock;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.sakura.stock.pojo.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: sakura
 * @date: 2024/3/17 17:37
 * @description: 测试EasyExcel
 */
public class TestEasyExcel {
    public List<User> init() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("张三" + i);
            user.setAge("18" + i);
            user.setAddress("上海" + i);
            user.setBirthday(new Date());
            users.add(user);
        }
        return users;
    }

    /**
     * 测试excel导出
     */
    @Test
    public void test01() {
        List<User> users = this.init();
        // 导出数据到excel
        EasyExcel.write("D://test.xlsx", User.class)
                .sheet("用户信息")
                .doWrite(users);
    }

    /**
     * 测试excel导入
     */
    @Test
    public void test02() {
        List<User> users = new ArrayList<>();
        // 导入数据到list
        EasyExcel.read("D://test.xlsx", User.class, new AnalysisEventListener<User>() {
            /**
             * 逐行读取excel，并封装（读取一行就调用一次该方法）
             * @param user
             * @param analysisContext
             */
            @Override
            public void invoke(User user, AnalysisContext analysisContext) {
                users.add(user);
            }

            /**
             * 所有行读取完毕后 回调方法（读取完成的通知）
             * @param analysisContext
             */
            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                System.out.println("读取完毕啦~");
            }
        }).sheet("用户信息").doRead();
    }
}
