package com.cpems.system.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 定额分析视图
 *
 * @Author cpems
 * @Date 2023/9/13 13:21
 */
@Data
public class QuotaAnalysisVo {
    private static final long serialVersionUID=1L;

    /**
     * 时间
     */
    private String date;

    /**
     * 单位
     */
    private String unit;

    /**
     * 实际能耗
     */
    private BigDecimal realEnergy;

    /**
     * 能耗环比
     */
    private BigDecimal chainEnergy;

    /**
     * 能耗同比
     */
    private BigDecimal yoyEnergy;

    /**
     * 日均定额
     */
    private BigDecimal quotaAverage;

    /**
     * 定额完成百分比
     */
    private String quotaPercent;
}
