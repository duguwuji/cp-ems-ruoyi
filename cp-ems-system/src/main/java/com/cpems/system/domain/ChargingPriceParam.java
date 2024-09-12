package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 充电策略参数信息对象 charging_price_param
 *
 * @author cpems
 * @date 2023-10-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("charging_price_param")
public class ChargingPriceParam extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 充电价格参数id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 充电价格策略id
     */
    private Long strategyId;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 时段标识（0：尖期，1：峰期，2：平期，3：谷期）
     */
    private String mark;
    /**
     * 电费单价
     */
    private BigDecimal elecPrice;
    /**
     * 服务费单价
     */
    private BigDecimal servicePrice;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

}
