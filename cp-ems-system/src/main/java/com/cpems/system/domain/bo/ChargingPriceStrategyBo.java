package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 充电价格策略信息业务对象 charging_price_strategy
 *
 * @author cpems
 * @date 2023-10-12
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ChargingPriceStrategyBo extends BaseEntity {

    /**
     * 充电价格策略id
     */
//    @NotNull(message = "充电价格策略id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 策略名称
     */
    @NotBlank(message = "策略名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String strategyName;

    /**
     * 充电站id
     */
    @NotNull(message = "充电站id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long stationId;

    /**
     * 充电站名称
     */
    @NotBlank(message = "充电站名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String stationName;

    /**
     * 计费模式（0：峰平谷模式，1：时段模式）
     */
    @NotBlank(message = "计费模式（0：峰平谷模式，1：时段模式）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String billModel;

    /**
     * 充电策略说明
     */
//    @NotBlank(message = "充电策略说明不能为空", groups = { AddGroup.class, EditGroup.class })
    private String description;

    /**
     * 策略状态（0：未使用，1：已使用）
     */
    @NotBlank(message = "策略状态（0：未使用，1：已使用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;


}
