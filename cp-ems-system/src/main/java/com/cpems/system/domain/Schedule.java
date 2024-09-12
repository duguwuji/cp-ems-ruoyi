package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 调度计划管理对象 schedule
 *
 * @author cpems
 * @date 2023-09-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("schedule")
public class Schedule extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 调度计划id
     */
    @TableId(value = "schedule_id")
    private Long scheduleId;
    /**
     * 计划编号
     */
    private String number;
    /**
     * 类型
     */
    private String type;
    /**
     * 计划周期
     */
    private String cycle;
    /**
     * 站所
     */
    private String station;
    /**
     * 状态
     */
    private String status;
    /**
     * 是否超时(0是，1不是)
     */
    private String isTimeout;
    /**
     * 计划名称
     */
    private String name;
    /**
     * 计划来源
     */
    private String source;
    /**
     * 计划内容
     */
    private String content;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 一级提醒
     */
    private Long firstReminder;
    /**
     * 二级提醒
     */
    private Long secondReminder;
    /**
     * 开工执行人
     */
    private String operator;
    /**
     * 开工时间
     */
    private Date commencementTime;
    /**
     * 归结执行人
     */
    private String terminator;
    /**
     * 归结时间
     */
    private Date resolutionTime;
    /**
     * 归结内容
     */
    private String resolutionContent;
    /**
     * 编制人
     */
    private String preparedBy;
    /**
     * 负责人
     */
    private String head;
    /**
     * 注意事项
     */
    private String remark;

}
