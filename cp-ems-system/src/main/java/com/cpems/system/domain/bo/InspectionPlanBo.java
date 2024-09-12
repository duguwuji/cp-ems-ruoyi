package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.List;

/**
 * 巡检计划业务对象 inspection_plan
 *
 * @author ruoyi
 * @date 2023-04-04
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class InspectionPlanBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 计划名称
     */
    @NotBlank(message = "计划名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String planName;

    /**
     * 计划内容
     */
//    @NotBlank(message = "计划内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String planContent;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String projectName;

    /**
     * 开始日期
     */
    @NotNull(message = "开始日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String startDate;

    /**
     * 结束日期
     */
    @NotNull(message = "结束日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String endDate;

    /**
     * 巡检周期
     */
    @NotBlank(message = "巡检周期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String inspectionCycle;

    /**
     * 巡检人
     */
    @NotBlank(message = "巡检人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String inspectionPerson;

    /**
     * 巡检人id
     */
//    @NotNull(message = "巡检人id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String userId;

    /**
     * 巡检人id
     */
//    @NotNull(message = "巡检人id不能为空", groups = { AddGroup.class, EditGroup.class })
    private List<String> userIds;

    /**
     * 巡检日期
     */
//    @NotBlank(message = "巡检日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String setDate;

    /**
     * 巡检日期
     */
//    @NotNull(message = "巡检日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private List<String> setDates;

    /**
     * 巡检固定时间
     */
//    @NotBlank(message = "巡检固定时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private String setTime;

    /**
     * 状态（0正常 1停用）
     */
//    @NotBlank(message = "状态（0正常 1停用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;



}
