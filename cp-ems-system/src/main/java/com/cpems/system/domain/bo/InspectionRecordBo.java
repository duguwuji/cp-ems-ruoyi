package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

/**
 * 巡检记录业务对象 inspection_record
 *
 * @author ruoyi
 * @date 2023-04-04
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class InspectionRecordBo extends BaseEntity {

    /**
     *
     */
//    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 计划名称
     */
    @NotBlank(message = "计划名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String planName;

    /**
     * 计划内容
     */
    @NotBlank(message = "计划内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String planContent;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String projectName;

    /**
     * 计划巡检时间
     */
//    @NotNull(message = "计划巡检时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private String planTime;

    /**
     * 实际巡检时间
     */
//    @NotNull(message = "实际巡检时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private String inspectionTime;

    /**
     * 巡检人
     */
//    @NotBlank(message = "巡检人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String inspectionPerson;

    /**
     * 巡检状态
     */
    @NotBlank(message = "巡检状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String inspectionStatus;

    /**
     * 巡检备注
     */
//    @NotBlank(message = "巡检备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String inspectionRemark;

    /**
     * 巡检人id
     */
//    @NotNull(message = "巡检人id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String userId;

    /**
     * 周期
     */
//    @NotBlank(message = "周期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String cycle;

    /**
     * 巡检周期
     */
//    @NotBlank(message = "巡检周期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String inspectionCycle;

    /**
     * 计划id
     */
//    @NotNull(message = "计划id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long planId;


}
