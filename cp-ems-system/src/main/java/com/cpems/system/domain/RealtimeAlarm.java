package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 实时报警对象 realtime_alarm
 *
 * @author cpems
 * @date 2023-04-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("realtime_alarm")
public class RealtimeAlarm extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 参数名称
     */
    private String paramName;
    /**
     * 报警时间
     */
    private Date alarmTime;
    /**
     * 报警信息
     */
    private String alarmInfo;
    /**
     * 报警等级
     */
    private String alarmLevel;
    /**
     * 报警区域
     */
    private String area;
    /**
     * 报警设备
     */
    private String equipment;
    /**
     * 报警值
     */
    private BigDecimal alarmVal;

}
