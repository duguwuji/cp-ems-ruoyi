package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 预案管理业务对象 pre_plan
 *
 * @author cpems
 * @date 2023-09-11
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PrePlanBo extends BaseEntity {

    /**
     * 预案id
     */
    @NotNull(message = "预案id不能为空", groups = { EditGroup.class })
    private Long prePlanId;

    /**
     * 预案编号
     */
    @NotBlank(message = "预案编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String number;

    /**
     * 预案类型
     */
//    @NotBlank(message = "预案类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;

    /**
     * 站名
     */
    @NotBlank(message = "站名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String station;

    /**
     * 设备
     */
//    @NotBlank(message = "设备不能为空", groups = { AddGroup.class, EditGroup.class })
    private String device;

    /**
     * 预案内容
     */
//    @NotBlank(message = "预案内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 编制人
     */
//    @NotBlank(message = "编制人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String preparedBy;

    /**
     * 预案关键词
     */
//    @NotBlank(message = "预案关键词不能为空", groups = { AddGroup.class, EditGroup.class })
    private String keywords;


}
