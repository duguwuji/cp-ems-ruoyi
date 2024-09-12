package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 报警规则对象 alarm_rule
 *
 * @author cpems
 * @date 2023-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("alarm_rule")
public class AlarmRule extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 参数id
     */
    private Long paramId;
    /**
     * 参数名称
     */
    private String paramName;
    /**
     * 报警类型
     */
    private String alarmType;
    /**
     * 报警等级
     */
    private String alarmLevel;
    /**
     * 事件类型
     */
    private String eventType;
    /**
     * 条件1
     */
    private String condition1;
    /**
     * 阈值1
     */
    private BigDecimal thresholdValue1;
    /**
     * 条件2
     */
    private String condition2;
    /**
     * 阈值2
     */
    private BigDecimal thresholdValue2;
    /**
     * 报警开关
     */
    private String alarmSwitch;
    /**
     * 自动创建工单开关
     */
    private String createOrderSwitch;

    /**
     * 提醒人
     */
    private String userId;

    /**
     * 报警信息
     */
    private String alarmInfo;

}
