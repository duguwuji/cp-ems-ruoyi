package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 充电价格策略信息对象 charging_price_strategy
 *
 * @author cpems
 * @date 2023-10-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("charging_price_strategy")
public class ChargingPriceStrategy extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 充电价格策略id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 策略名称
     */
    private String strategyName;
    /**
     * 充电站id
     */
    private Long stationId;
    /**
     * 充电站名称
     */
    private String stationName;
    /**
     * 计费模式（0：峰平谷模式，1：时段模式）
     */
    private String billModel;
    /**
     * 充电策略说明
     */
    private String description;
    /**
     * 策略状态（0：未使用，1：已使用）
     */
    private String status;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

}
