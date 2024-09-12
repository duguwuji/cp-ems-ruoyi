package com.cpems.system.domain.vo;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 调度计划管理视图对象 schedule
 *
 * @author ruoyi
 * @date 2023-09-11
 */
@Data
@ExcelIgnoreUnannotated
public class ScheduleVo {

    private static final long serialVersionUID = 1L;

    /**
     * 调度计划id
     */
    @ExcelProperty(value = "调度计划id")
    private Long scheduleId;

    /**
     * 计划编号
     */
    @ExcelProperty(value = "计划编号")
    private String number;

    /**
     * 类型
     */
    @ExcelProperty(value = "类型")
    private String type;

    /**
     * 计划周期
     */
    @ExcelProperty(value = "计划周期")
    private String cycle;

    /**
     * 站所
     */
    @ExcelProperty(value = "站所")
    private String station;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 是否超时(0是，1不是)
     */
    @ExcelProperty(value = "是否超时(0是，1不是)")
    private String isTimeout;

    /**
     * 计划名称
     */
    @ExcelProperty(value = "计划名称")
    private String name;

    /**
     * 计划来源
     */
    @ExcelProperty(value = "计划来源")
    private String source;

    /**
     * 计划内容
     */
    @ExcelProperty(value = "计划内容")
    private String content;

    /**
     * 开始时间
     */
    @ExcelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ExcelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 一级提醒
     */
    @ExcelProperty(value = "一级提醒")
    private Long firstReminder;

    /**
     * 二级提醒
     */
    @ExcelProperty(value = "二级提醒")
    private Long secondReminder;

    /**
     * 开工执行人
     */
    @ExcelProperty(value = "开工执行人")
    private String operator;

    /**
     * 开工时间
     */
    @ExcelProperty(value = "开工时间")
    private Date commencementTime;

    /**
     * 归结执行人
     */
    @ExcelProperty(value = "归结执行人")
    private String terminator;

    /**
     * 归结时间
     */
    @ExcelProperty(value = "归结时间")
    private Date resolutionTime;

    /**
     * 归结内容
     */
    @ExcelProperty(value = "归结内容")
    private String resolutionContent;

    /**
     * 编制人
     */
    @ExcelProperty(value = "编制人")
    private String preparedBy;

    /**
     * 负责人
     */
    @ExcelProperty(value = "负责人")
    private String head;

    /**
     * 注意事项
     */
    @ExcelProperty(value = "注意事项")
    private String remark;


}
