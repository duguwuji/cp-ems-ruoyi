package com.cpems.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.cpems.common.annotation.ExcelDictFormat;
import com.cpems.common.convert.ExcelDictConvert;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;


/**
 * 充电价格策略信息视图对象 charging_price_strategy
 *
 * @author cpems
 * @date 2023-10-12
 */
@Data
@ExcelIgnoreUnannotated
public class ChargingPriceStrategyVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 充电价格策略id
     */
    @ExcelProperty(value = "充电价格策略id")
    private Long id;

    /**
     * 策略名称
     */
    @ExcelProperty(value = "策略名称")
    private String strategyName;

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
     * 计费模式（0：峰平谷模式，1：时段模式）
     */
    @ExcelProperty(value = "计费模式", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=峰平谷模式，1=时段模式")
    private String billModel;

    /**
     * 充电策略说明
     */
    @ExcelProperty(value = "充电策略说明")
    private String description;

    /**
     * 策略状态（0：未使用，1：已使用）
     */
    @ExcelProperty(value = "策略状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=未使用，1=已使用")
    private String status;


}
