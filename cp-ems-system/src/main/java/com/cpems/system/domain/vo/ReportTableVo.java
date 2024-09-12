package com.cpems.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 分析报告视图
 *
 * @Author cpems
 * @Date 2023/7/18 10:32
 */
@Data
@ExcelIgnoreUnannotated
public class ReportTableVo {
    private static final long serialVersionUID = 1L;

    /**
     *分类
     */
    @ExcelProperty(value = "分类")
    private String type;

    /**
     *能耗
     */
    @ExcelProperty(value = "能耗")
    private BigDecimal data;

    /**
     *环比
     */
    @ExcelProperty(value = "环比")
    private BigDecimal chain;

    /**
     *同比
     */
    @ExcelProperty(value = "同比")
    private BigDecimal same;
}
