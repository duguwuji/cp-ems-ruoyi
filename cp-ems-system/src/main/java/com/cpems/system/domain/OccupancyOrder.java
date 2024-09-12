package com.cpems.system.domain;
import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 占位订单信息对象 occupancy_order
 *
 * @author cpems
 * @date 2023-10-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("occupancy_order")
public class OccupancyOrder extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 占位订单id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 占位订单编号
     */
    private Long occupancyNo;
    /**
     * 充电订单号
     */
    private Long orderNo;
    /**
     * 占位时长
     */
    private Long duration;
    /**
     * 占位费用
     */
    private BigDecimal fee;
    /**
     * 是否产生占位费
     */
    private String isFee;
    /**
     * 支付时间
     */
    private Date payTime;
    /**
     * 结算状态（0：未结算，1：已结算）
     */
    private String settleStatus;
    /**
     * 占位订单状态（0：占位中，1：收费中，2：已结算）
     */
    private String orderStatus;
    /**
     * 订单备注
     */
    private String remark;
    /**
     * 结束原因
     */
    private String endReason;

}
