package com.cpems.system.domain.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 实时报警视图对象 realtime_alarm
 *
 * @author ruoyi
 * @date 2023-04-04
 */
@Data
@ExcelIgnoreUnannotated
public class RealtimeAlarmVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 参数名称
     */
    @ExcelProperty(value = "参数名称")
    private String paramName;

    /**
     * 报警时间
     */
    @ExcelProperty(value = "报警时间")
    private Date alarmTime;

    /**
     * 报警信息
     */
    @ExcelProperty(value = "报警信息")
    private String alarmInfo;

    /**
     * 报警等级
     */
    @ExcelProperty(value = "报警等级")
    private String alarmLevel;

    /**
     * 报警区域
     */
    @ExcelProperty(value = "报警区域")
    private String area;

    /**
     * 报警设备
     */
    @ExcelProperty(value = "报警设备")
    private String equipment;

    /**
     * 报警值
     */
    @ExcelProperty(value = "报警值")
    private BigDecimal alarmVal;
    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    private Long count;
    /**
     * 占比
     */
    @ExcelProperty(value = "占比")
    private String proportion;

    /**
     * 上期数量
     */
    @ExcelProperty(value = "上期数量")
    private Long lastCount;

    /**
     * 统计横坐标日期
     */
    @ExcelProperty(value = "统计横坐标日期")
    private String name;


}
