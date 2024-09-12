package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 阶梯计费参数信息对象 charging_step
 *
 * @author cpems
 * @date 2023-10-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("charging_step")
public class ChargingStep extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 阶梯计费id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 计费方案id
     */
    private Long chargingId;
    /**
     * 开始用量
     */
    private Integer startStep;
    /**
     * 结束用量
     */
    private Integer endStep;
    /**
     * 差价
     */
    private BigDecimal priceDifference;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

}
