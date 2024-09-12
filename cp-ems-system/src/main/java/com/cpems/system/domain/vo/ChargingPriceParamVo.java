package com.cpems.system.domain.vo;

import java.math.BigDecimal;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.cpems.common.annotation.ExcelDictFormat;
import com.cpems.common.convert.ExcelDictConvert;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;


/**
 * 充电策略参数信息视图对象 charging_price_param
 *
 * @author cpems
 * @date 2023-10-12
 */
@Data
@ExcelIgnoreUnannotated
public class ChargingPriceParamVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 充电价格参数id
     */
    @ExcelProperty(value = "充电价格参数id")
    private Long id;

    /**
     * 充电价格策略id
     */
    @ExcelProperty(value = "充电价格策略id")
    private Long strategyId;

    /**
     * 开始时间
     */
    @ExcelProperty(value = "开始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @ExcelProperty(value = "结束时间")
    private String endTime;

    /**
     * 时段标识（0：尖期，1：峰期，2：平期，3：谷期）
     */
    @ExcelProperty(value = "时段标识", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=尖期，1=峰期，2=平期，3=谷期")
    private String mark;

    /**
     * 电费单价
     */
    @ExcelProperty(value = "电费单价")
    private BigDecimal elecPrice;

    /**
     * 服务费单价
     */
    @ExcelProperty(value = "服务费单价")
    private BigDecimal servicePrice;


}
