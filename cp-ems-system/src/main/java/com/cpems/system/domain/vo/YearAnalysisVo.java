package com.cpems.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 同比分析VO
 *
 * @Author cpems
 * @Date 2023/6/27 14:03
 */
@Data
@ExcelIgnoreUnannotated
public class YearAnalysisVo {
    private static final long serialVersionUID = 1L;

    /**
     * 月份
     */
    @ExcelProperty(value = "月份")
    private Integer month;

    /**
     * 本期
     */
    @ExcelProperty(value = "本期")
    private String currentPeriod;

    /**
     * 同期
     */
    @ExcelProperty(value = "同期")
    private String correspondingPeriod;

    /**
     * 同比
     */
    @ExcelProperty(value = "同比")
    private BigDecimal yoy;


}
