package com.cpems.system.domain.vo;

import lombok.Data;

import java.util.Map;

/**
 * 分项分析数据视图
 *
 * @Author cpems
 * @Date 2023/9/5 11:00
 */
@Data
public class ItemizeVo {
    private static final long serialVersionUID=1L;

    /**
     * 分项ID
     */
    private String itemizeId;

    /**
     * 分项名称
     */
    private String itemizeName;

    /**
     * 分项统计
     */
    private Map<String,Object> itemizeCount;

    /**
     * 分项占比
     */
    private Object itemizeTotal;

    /**
     * 当日能耗
     */
    private Object nowEnergy;

    /**
     * 昨日同期能耗
     */
    private Object lastEnergy;

    /**
     * 日趋势
     */
    private Object nowTrend;

    /**
     * 日趋势百分比
     */
    private Object nowPer;

    /**
     * 当月能耗
     */
    private Object nowMonthEnergy;

    /**
     * 上月同期能耗
     */
    private Object lastMonthEnergy;

    /**
     * 月趋势
     */
    private Object monthTrend;

    /**
     * 月趋势百分比
     */
    private Object monthPer;

    /**
     * 当年能耗
     */
    private Object nowYearEnergy;

    /**
     * 上年同期能耗
     */
    private Object lastYearEnergy;

    /**
     * 年趋势
     */
    private Object yearTrend;

    /**
     * 年趋势百分比
     */
    private Object yearPer;
}
