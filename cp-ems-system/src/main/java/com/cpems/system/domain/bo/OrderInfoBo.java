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
 * @Author cpems
 * @Date 2023/10/7 13:51
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderInfoBo extends BaseEntity {

    /**
     * 实时订单id
     */
    @NotNull(message = "实时订单id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 订单编号
     */
    @NotBlank(message = "订单编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long orderNo;

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 用户名称
     */
    @NotBlank(message = "用户名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String userName;

    /**
     * 电话
     */
    @NotBlank(message = "电话不能为空", groups = { AddGroup.class, EditGroup.class })
    private String phone;

    /**
     * 商户id
     */
    @NotNull(message = "商户id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long merchantId;

    /**
     * 商户名称
     */
    @NotBlank(message = "商户名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String merchantName;

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
     * 充电桩id
     */
    @NotNull(message = "充电桩id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long pileId;

    /**
     * 充电桩名称
     */
    @NotBlank(message = "充电桩名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String pileName;

    /**
     * 充电开始时间
     */
    @NotNull(message = "充电开始时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date startTime;

    /**
     * 充电结束时间
     */
    @NotNull(message = "充电结束时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date endTime;

    /**
     * 车辆id
     */
    @NotNull(message = "车辆id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long carId;

    /**
     * 车牌号
     */
    @NotBlank(message = "车牌号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String carNo;

    /**
     * VIN码
     */
    @NotBlank(message = "VIN码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String carVin;

    /**
     * 充电方式（0：即插即充）
     */
    @NotBlank(message = "充电方式不能为空", groups = { AddGroup.class, EditGroup.class })
    private String chargeMethod;

    /**
     * 结算类型（0：正常结束结算，1：手动结算，2：延迟结算，3：异常单结算，4：离线结算）
     */
    @NotBlank(message = "结算类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String settleType;

    /**
     * 支付方式（0：账户余额，1：微信支付，2：支付宝支付）
     */
    @NotBlank(message = "支付方式不能为空", groups = { AddGroup.class, EditGroup.class })
    private String payType;

    /**
     * 订单结算时间
     */
    @NotNull(message = "订单结算时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date settleTime;

    /**
     * 结算金额
     */
    @NotNull(message = "结算金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal settlePrice;

    /**
     * 实际支付金额
     */
    @NotNull(message = "实际支付金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal paidPrice;

    /**
     * 优惠金额
     */
    @NotNull(message = "优惠金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal discountAmt;

    /**
     * 电费
     */
    @NotNull(message = "电费不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal elecAmt;

    /**
     * 服务费
     */
    @NotNull(message = "服务费不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal serveAmt;

    /**
     * 订单状态（0：充电中，1：待支付，2：已完成，3：失败，4：已退款）
     */
    @NotBlank(message = "订单状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String orderStatus;

    /**
     * 充电时长
     */
    @NotNull(message = "充电时长不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long chargeDuration;

    /**
     * 总充电量
     */
    @NotNull(message = "总充电量不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal energy;

    /**
     * 订单来源（0：微信小程序，1：PC端，2：app）
     */
    @NotBlank(message = "订单来源不能为空", groups = { AddGroup.class, EditGroup.class })
    private String orderSource;

    /**
     * 账户余额
     */
    @NotNull(message = "账户余额不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal settleBalance;

    /**
     * 开始SOC
     */
    @NotNull(message = "开始SOC不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal startSoc;

    /**
     * 结束SOC
     */
    @NotNull(message = "结束SOC不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal endSoc;

    /**
     * 异常原因
     */
    @NotBlank(message = "异常原因不能为空", groups = { AddGroup.class, EditGroup.class })
    private String abnoCause;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;

}
