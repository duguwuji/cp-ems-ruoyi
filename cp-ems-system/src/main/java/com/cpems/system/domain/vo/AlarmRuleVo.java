package com.cpems.system.domain.vo;

import java.math.BigDecimal;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 报警规则视图对象 alarm_rule
 *
 * @author ruoyi
 * @date 2023-04-06
 */
@Data
@ExcelIgnoreUnannotated
public class AlarmRuleVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 参数id
     */
    @ExcelProperty(value = "参数id")
    private Long paramId;

    /**
     * 参数名称
     */
    @ExcelProperty(value = "参数名称")
    private String paramName;

    /**
     * 报警类型
     */
    @ExcelProperty(value = "报警类型")
    private String alarmType;

    /**
     * 报警等级
     */
    @ExcelProperty(value = "报警等级")
    private String alarmLevel;

    /**
     * 事件类型
     */
    @ExcelProperty(value = "事件类型")
    private String eventType;

    /**
     * 条件1
     */
    @ExcelProperty(value = "条件1")
    private String condition1;

    /**
     * 阈值1
     */
    @ExcelProperty(value = "阈值1")
    private BigDecimal thresholdValue1;

    /**
     * 条件2
     */
    @ExcelProperty(value = "条件2")
    private String condition2;

    /**
     * 阈值2
     */
    @ExcelProperty(value = "阈值2")
    private BigDecimal thresholdValue2;

    /**
     * 报警开关
     */
    @ExcelProperty(value = "报警开关")
    private String alarmSwitch;

    /**
     * 自动创建工单开关
     */
    @ExcelProperty(value = "自动创建工单开关")
    private String createOrderSwitch;

    /**
     * 提醒人
     */
    @ExcelProperty(value = "提醒人")
    private String userId;

    /**
     * 提醒人名称
     */
    private String nickName;

    /**
     * 报警信息
     */
    private String alarmInfo;

}
