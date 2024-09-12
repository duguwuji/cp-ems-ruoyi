package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;

/**
 * 维修工单业务对象 repair_order
 *
 * @author ruoyi
 * @date 2023-04-04
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class RepairOrderBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 工单编号
     */
    @NotBlank(message = "工单编号不能为空", groups = {AddGroup.class, EditGroup.class})
    private String orderNo;

    /**
     * 工单内容
     */
    @NotBlank(message = "工单内容不能为空", groups = {AddGroup.class, EditGroup.class})
    private String orderContent;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String projectName;

    /**
     * 附件
     */
//    @NotBlank(message = "附件不能为空", groups = { AddGroup.class, EditGroup.class })
    private String annex;

    /**
     * 派单时间
     */
//    @NotNull(message = "派单时间不能为空", groups = {AddGroup.class, EditGroup.class})
    private String assignTime;

    /**
     * 完成时间
     */
//    @NotNull(message = "完成时间不能为空", groups = {AddGroup.class, EditGroup.class})
    private String finishTime;

    /**
     * 完成人
     */
//    @NotBlank(message = "完成人不能为空", groups = {AddGroup.class, EditGroup.class})
    private String finishBy;

    /**
     * 工单状态
     */
//    @NotBlank(message = "工单状态不能为空", groups = {AddGroup.class, EditGroup.class})
    private String orderStatus;

    /**
     * 工单备注
     */
//    @NotBlank(message = "工单备注不能为空", groups = {AddGroup.class, EditGroup.class})
    private String orderRemark;

    /**
     * 完成人id
     */
//    @NotBlank(message = "完成人不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long userId;
}
