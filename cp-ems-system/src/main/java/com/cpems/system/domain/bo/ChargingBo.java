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
 * 计费方案信息业务对象 charging
 *
 * @author ruoyi
 * @date 2023-10-30
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ChargingBo extends BaseEntity {

    /**
     * 计费方案ID
     */
    @NotNull(message = "计费方案ID不能为空", groups = { EditGroup.class })
    private Long chargingId;

    /**
     * 类型
     */
    @NotBlank(message = "类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date startDate;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date endDate;

    /**
     * 单价
     */
//    @NotNull(message = "单价不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal unitPrice;

    /**
     * 尖峰平谷开关（0正常 1停用）
     */
//    @NotBlank(message = "尖峰平谷开关（0正常 1停用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String periodSwitch;

    /**
     * 尖时单价
     */
//    @NotNull(message = "尖时单价不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal sharpPrice;

    /**
     * 峰时单价
     */
//    @NotNull(message = "峰时单价不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal peekPrice;

    /**
     * 平时单价
     */
//    @NotNull(message = "平时单价不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal ordinaryPrice;

    /**
     * 谷时单价
     */
//    @NotNull(message = "谷时单价不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal valleyPrice;

    /**
     * 阶梯计费开关（0正常 1停用）
     */
//    @NotBlank(message = "阶梯计费开关（0正常 1停用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String stepSwitch;

    /**
     * 状态（0正常 1停用）
     */
//    @NotBlank(message = "状态（0正常 1停用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

    /**
     * 备注
     */
//    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
