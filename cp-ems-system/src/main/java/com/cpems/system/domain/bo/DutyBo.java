package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 值班管理业务对象 duty
 *
 * @author ruoyi
 * @date 2023-09-11
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class DutyBo extends BaseEntity {

    /**
     * 值班id
     */
    @NotNull(message = "值班id不能为空", groups = { EditGroup.class })
    private Long dutyId;

    /**
     * 值班日期
     */
    @NotBlank(message = "值班日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String dutyDate;

    /**
     * 值班人员
     */
    @NotBlank(message = "值班人员不能为空", groups = { AddGroup.class, EditGroup.class })
    private String onDuty;

    /**
     * 值班时间段
     */
    @NotBlank(message = "值班时间段不能为空", groups = { AddGroup.class, EditGroup.class })
    private String dutyPeriod;

    /**
     * 值班类型
     */
//    @NotBlank(message = "值班类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;

    /**
     * 值班状态
     */
//    @NotBlank(message = "值班状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

    /**
     * 替班人员
     */
    @NotBlank(message = "替班人员不能为空", groups = { AddGroup.class, EditGroup.class })
    private String relief;

    /**
     * 值班备注
     */
    @NotBlank(message = "值班备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
