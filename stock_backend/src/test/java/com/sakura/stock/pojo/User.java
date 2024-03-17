package com.sakura.stock.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: sakura
 * @date: 2024/3/17 17:38
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@HeadRowHeight(35) // 标题行高
@ContentRowHeight(20) // 内容行高
@ColumnWidth(50) // 列宽
public class User implements Serializable {
    @ExcelProperty(value = {"用户基本信息", "用户名"}, index = 0)
    private String username;
    @ExcelProperty(value = {"用户基本信息", "年龄"}, index = 1)
    private String age;
    @ExcelProperty(value = {"用户基本信息", "地址"}, index = 2)
    private String address;
    @ExcelProperty(value = {"用户基本信息", "生日"}, index = 3)
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelIgnore
    private Date birthday;
}
