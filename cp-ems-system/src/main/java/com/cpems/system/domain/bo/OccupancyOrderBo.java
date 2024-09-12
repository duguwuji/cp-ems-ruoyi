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
 * 占位订单信息业务对象 occupancy_order
 *
 * @author cpems
 * @date 2023-10-08
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class OccupancyOrderBo extends BaseEntity {

    /**
     * 占位订单id
     */
    @NotNull(message = "占位订单id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 占位订单编号
     */
    @NotBlank(message = "占位订单编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long occupancyNo;

    /**
     * 充电订单号
     */
    @NotNull(message = "充电订单号不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long orderNo;

    /**
     * 占位时长
     */
    @NotNull(message = "占位时长不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long duration;

    /**
     * 占位费用
     */
    @NotNull(message = "占位费用不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal fee;

    /**
     * 是否产生占位费
     */
    @NotBlank(message = "是否产生占位费不能为空", groups = { AddGroup.class, EditGroup.class })
    private String isFee;

    /**
     * 支付时间
     */
    @NotNull(message = "支付时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date payTime;

    /**
     * 结算状态（0：未结算，1：已结算）
     */
    @NotBlank(message = "结算状态（0：未结算，1：已结算）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String settleStatus;

    /**
     * 占位订单状态（0：占位中，1：收费中，2：已结算）
     */
    @NotBlank(message = "占位订单状态（0：占位中，1：收费中，2：已结算）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String orderStatus;

    /**
     * 订单备注
     */
    @NotBlank(message = "订单备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;

    /**
     * 结束原因
     */
    @NotBlank(message = "结束原因不能为空", groups = { AddGroup.class, EditGroup.class })
    private String endReason;


}
