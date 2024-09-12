package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 实时报警业务对象 realtime_alarm
 *
 * @author cpems
 * @date 2023-04-04
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class RealtimeAlarmBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 参数名称
     */
    @NotBlank(message = "参数名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String paramName;

    /**
     * 报警时间
     */
    @NotNull(message = "报警时间不能为空", groups = {AddGroup.class, EditGroup.class})
    private Date alarmTime;

    /**
     * 报警信息
     */
    @NotBlank(message = "报警信息不能为空", groups = {AddGroup.class, EditGroup.class})
    private String alarmInfo;

    /**
     * 报警等级
     */
    @NotBlank(message = "报警等级不能为空", groups = {AddGroup.class, EditGroup.class})
    private String alarmLevel;

    /**
     * 报警区域
     */
    @NotBlank(message = "报警区域不能为空", groups = {AddGroup.class, EditGroup.class})
    private String area;

    /**
     * 报警设备
     */
    @NotBlank(message = "报警设备不能为空", groups = {AddGroup.class, EditGroup.class})
    private String equipment;

    /**
     * 报警值
     */
    @NotNull(message = "报警值不能为空", groups = {AddGroup.class, EditGroup.class})
    private BigDecimal alarmVal;


}
