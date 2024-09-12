package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.math.BigDecimal;

/**
 * 充电策略参数信息业务对象 charging_price_param
 *
 * @author cpems
 * @date 2023-10-12
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ChargingPriceParamBo extends BaseEntity {

    /**
     * 充电价格参数id
     */
//    @NotNull(message = "充电价格参数id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 充电价格策略id
     */
    @NotNull(message = "充电价格策略id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long strategyId;

    /**
     * 开始时间
     */
    @NotBlank(message = "开始时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private String startTime;

    /**
     * 结束时间
     */
    @NotBlank(message = "结束时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private String endTime;

    /**
     * 时段标识（0：尖期，1：峰期，2：平期，3：谷期）
     */
    @NotBlank(message = "时段标识（0：尖期，1：峰期，2：平期，3：谷期）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String mark;

    /**
     * 电费单价
     */
    @NotNull(message = "电费单价不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal elecPrice;

    /**
     * 服务费单价
     */
    @NotNull(message = "服务费单价不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal servicePrice;


}
