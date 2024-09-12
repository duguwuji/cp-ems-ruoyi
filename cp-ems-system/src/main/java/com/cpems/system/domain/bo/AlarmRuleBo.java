package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.math.BigDecimal;

/**
 * 报警规则业务对象 alarm_rule
 *
 * @author cpems
 * @date 2023-04-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmRuleBo extends BaseEntity {

    /**
     *
     */
//    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 参数id
     */
//    @NotNull(message = "参数id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long paramId;

    /**
     * 参数名称
     */
    @NotBlank(message = "参数名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String paramName;

    /**
     * 报警类型
     */
//    @NotBlank(message = "报警类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String alarmType;

    /**
     * 报警等级
     */
    @NotBlank(message = "报警等级不能为空", groups = { AddGroup.class, EditGroup.class })
    private String alarmLevel;

    /**
     * 事件类型
     */
    @NotBlank(message = "事件类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String eventType;

    /**
     * 条件1
     */
    @NotBlank(message = "条件1不能为空", groups = { AddGroup.class, EditGroup.class })
    private String condition1;

    /**
     * 阈值1
     */
    @NotNull(message = "阈值1不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal thresholdValue1;

    /**
     * 条件2
     */
    @NotBlank(message = "条件2不能为空", groups = { AddGroup.class, EditGroup.class })
    private String condition2;

    /**
     * 阈值2
     */
    @NotNull(message = "阈值2不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal thresholdValue2;

    /**
     * 报警开关
     */
    @NotBlank(message = "报警开关不能为空", groups = { AddGroup.class, EditGroup.class })
    private String alarmSwitch;

    /**
     * 自动创建工单开关
     */
    @NotBlank(message = "自动创建工单开关不能为空", groups = { AddGroup.class, EditGroup.class })
    private String createOrderSwitch;

    /**
     * 提醒人
     */
    @NotBlank(message = "提醒人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String userId;


    /**
     * 报警信息
     */
    private String alarmInfo;
}
