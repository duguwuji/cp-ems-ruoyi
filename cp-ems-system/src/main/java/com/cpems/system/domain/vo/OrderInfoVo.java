package com.cpems.system.domain.vo;

import java.math.BigDecimal;
import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.cpems.common.annotation.ExcelDictFormat;
import com.cpems.common.convert.ExcelDictConvert;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * @Author cpems
 * @Date 2023/10/7 13:49
 **/
@Data
@ExcelIgnoreUnannotated
public class OrderInfoVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 实时订单id
     */
    @ExcelProperty(value = "实时订单id")
    private Long id;

    /**
     * 订单编号
     */
    @ExcelProperty(value = "订单编号")
    private Long orderNo;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    private Long userId;

    /**
     * 用户名称
     */
    @ExcelProperty(value = "用户名称")
    private String userName;

    /**
     * 电话
     */
    @ExcelProperty(value = "电话")
    private String phone;

    /**
     * 商户id
     */
    @ExcelProperty(value = "商户id")
    private Long merchantId;

    /**
     * 商户名称
     */
    @ExcelProperty(value = "商户名称")
    private String merchantName;

    /**
     * 充电站id
     */
    @ExcelProperty(value = "充电站id")
    private Long stationId;

    /**
     * 充电站名称
     */
    @ExcelProperty(value = "充电站名称")
    private String stationName;

    /**
     * 充电桩id
     */
    @ExcelProperty(value = "充电桩id")
    private Long pileId;

    /**
     * 充电桩名称
     */
    @ExcelProperty(value = "充电桩名称")
    private String pileName;

    /**
     * 充电开始时间
     */
    @ExcelProperty(value = "充电开始时间")
    private Date startTime;

    /**
     * 充电结束时间
     */
    @ExcelProperty(value = "充电结束时间")
    private Date endTime;

    /**
     * 车辆id
     */
    @ExcelProperty(value = "车辆id")
    private Long carId;

    /**
     * 车牌号
     */
    @ExcelProperty(value = "车牌号")
    private String carNo;

    /**
     * VIN码
     */
    @ExcelProperty(value = "VIN码")
    private String carVin;

    /**
     * 充电方式（0：即插即充）
     */
    @ExcelProperty(value = "充电方式", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=即插即充")
    private String chargeMethod;

    /**
     * 结算类型（0：正常结束结算，1：手动结算，2：延迟结算，3：异常单结算，4：离线结算）
     */
    @ExcelProperty(value = "结算类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常结束结算，1=手动结算，2=延迟结算，3=异常单结算，4=离线结算")
    private String settleType;

    /**
     * 支付方式（0：账户余额，1：微信支付，2：支付宝支付）
     */
    @ExcelProperty(value = "支付方式", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=账户余额，1=微信支付，2=支付宝支付")
    private String payType;

    /**
     * 订单结算时间
     */
    @ExcelProperty(value = "订单结算时间")
    private Date settleTime;

    /**
     * 结算金额
     */
    @ExcelProperty(value = "结算金额")
    private BigDecimal settlePrice;

    /**
     * 实际支付金额
     */
    @ExcelProperty(value = "实际支付金额")
    private BigDecimal paidPrice;

    /**
     * 优惠金额
     */
    @ExcelProperty(value = "优惠金额")
    private BigDecimal discountAmt;

    /**
     * 电费
     */
    @ExcelProperty(value = "电费")
    private BigDecimal elecAmt;

    /**
     * 服务费
     */
    @ExcelProperty(value = "服务费")
    private BigDecimal serveAmt;

    /**
     * 订单状态（0：充电中，1：待支付，2：已完成，3：失败，4：已退款）
     */
    @ExcelProperty(value = "订单状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=充电中，1=待支付，2=已完成，3=失败，4=已退款")
    private String orderStatus;

    /**
     * 充电时长
     */
    @ExcelProperty(value = "充电时长")
    private Long chargeDuration;

    /**
     * 总充电量
     */
    @ExcelProperty(value = "总充电量")
    private BigDecimal energy;

    /**
     * 订单来源（0：微信小程序，1：PC端，2：app）
     */
    @ExcelProperty(value = "订单来源", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=微信小程序，1=PC端，2=app")
    private String orderSource;

    /**
     * 账户余额
     */
    @ExcelProperty(value = "账户余额")
    private BigDecimal settleBalance;

    /**
     * 开始SOC
     */
    @ExcelProperty(value = "开始SOC")
    private BigDecimal startSoc;

    /**
     * 结束SOC
     */
    @ExcelProperty(value = "结束SOC")
    private BigDecimal endSoc;

    /**
     * 异常原因
     */
    @ExcelProperty(value = "异常原因")
    private String abnoCause;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 商户信息
     */
    private MerchantVo merchant;

}
