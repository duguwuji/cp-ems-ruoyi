package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.math.BigDecimal;

/**
 * 阶梯计费参数信息业务对象 charging_step
 *
 * @author ruoyi
 * @date 2023-10-31
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ChargingStepBo extends BaseEntity {

    /**
     * 阶梯计费id
     */
    @NotNull(message = "阶梯计费id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 计费方案id
     */
    @NotNull(message = "计费方案id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long chargingId;

    /**
     * 开始用量
     */
    @NotNull(message = "开始用量不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer startStep;

    /**
     * 结束用量
     */
    @NotNull(message = "结束用量不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer endStep;

    /**
     * 差价
     */
    @NotNull(message = "差价不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal priceDifference;


}
