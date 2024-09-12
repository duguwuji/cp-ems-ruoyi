package com.cpems.system.domain.vo;
import java.math.BigDecimal;
import java.util.Date;

import com.cpems.common.core.domain.BaseEntity;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.cpems.common.annotation.ExcelDictFormat;
import com.cpems.common.convert.ExcelDictConvert;
import lombok.Data;


/**
 * 占位订单信息视图对象 occupancy_order
 *
 * @author cpems
 * @date 2023-10-08
 */
@Data
@ExcelIgnoreUnannotated
public class OccupancyOrderVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 占位订单id
     */
    @ExcelProperty(value = "占位订单id")
    private Long id;

    /**
     * 占位订单编号
     */
    @ExcelProperty(value = "占位订单编号")
    private Long occupancyNo;

    /**
     * 充电订单号
     */
    @ExcelProperty(value = "充电订单号")
    private Long orderNo;

    /**
     * 占位时长
     */
    @ExcelProperty(value = "占位时长")
    private Long duration;

    /**
     * 占位费用
     */
    @ExcelProperty(value = "占位费用")
    private BigDecimal fee;

    /**
     * 是否产生占位费
     */
    @ExcelProperty(value = "是否产生占位费")
    private String isFee;

    /**
     * 支付时间
     */
    @ExcelProperty(value = "支付时间")
    private Date payTime;

    /**
     * 结算状态（0：未结算，1：已结算）
     */
    @ExcelProperty(value = "结算状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=未结算，1=已结算")
    private String settleStatus;

    /**
     * 占位订单状态（0：占位中，1：收费中，2：已结算）
     */
    @ExcelProperty(value = "占位订单状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=占位中，1=收费中，2=已结算")
    private String orderStatus;

    /**
     * 订单备注
     */
    @ExcelProperty(value = "订单备注")
    private String remark;

    /**
     * 结束原因
     */
    @ExcelProperty(value = "结束原因")
    private String endReason;

    /**
     * 充电订单信息
     */
    private OrderInfoVo orderInfo;


}
