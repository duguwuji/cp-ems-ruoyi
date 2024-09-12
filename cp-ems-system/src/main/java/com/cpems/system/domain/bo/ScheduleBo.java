package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

/**
 * 调度计划管理业务对象 schedule
 *
 * @author cpems
 * @date 2023-09-11
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduleBo extends BaseEntity {

    /**
     * 调度计划id
     */
    @NotNull(message = "调度计划id不能为空", groups = { EditGroup.class })
    private Long scheduleId;

    /**
     * 计划编号
     */
    @NotBlank(message = "计划编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String number;

    /**
     * 类型
     */
//    @NotBlank(message = "类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;

    /**
     * 计划周期
     */
    @NotBlank(message = "计划周期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String cycle;

    /**
     * 站所
     */
    @NotBlank(message = "站所不能为空", groups = { AddGroup.class, EditGroup.class })
    private String station;

    /**
     * 状态
     */
//    @NotBlank(message = "状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

    /**
     * 是否超时(0是，1不是)
     */
//    @NotBlank(message = "是否超时(0是，1不是)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String isTimeout;

    /**
     * 计划名称
     */
    @NotBlank(message = "计划名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 计划来源
     */
    @NotBlank(message = "计划来源不能为空", groups = { AddGroup.class, EditGroup.class })
    private String source;

    /**
     * 计划内容
     */
    @NotBlank(message = "计划内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date endTime;

    /**
     * 一级提醒
     */
    @NotNull(message = "一级提醒不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long firstReminder;

    /**
     * 二级提醒
     */
    @NotNull(message = "二级提醒不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long secondReminder;

    /**
     * 开工执行人
     */
    @NotBlank(message = "开工执行人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String operator;

    /**
     * 开工时间
     */
    @NotNull(message = "开工时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date commencementTime;

    /**
     * 归结执行人
     */
    @NotBlank(message = "归结执行人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String terminator;

    /**
     * 归结时间
     */
    @NotNull(message = "归结时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date resolutionTime;

    /**
     * 归结内容
     */
    @NotBlank(message = "归结内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String resolutionContent;

    /**
     * 编制人
     */
    @NotBlank(message = "编制人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String preparedBy;

    /**
     * 负责人
     */
    @NotBlank(message = "负责人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String head;

    /**
     * 注意事项
     */
    @NotBlank(message = "注意事项不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
