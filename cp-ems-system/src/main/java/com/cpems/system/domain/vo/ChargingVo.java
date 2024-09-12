package com.cpems.system.domain.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.cpems.common.annotation.ExcelDictFormat;
import com.cpems.common.convert.ExcelDictConvert;
import lombok.Data;


/**
 * 计费方案信息视图对象 charging
 *
 * @author ruoyi
 * @date 2023-10-30
 */
@Data
@ExcelIgnoreUnannotated
public class ChargingVo {

    private static final long serialVersionUID = 1L;

    /**
     * 计费方案ID
     */
    @ExcelProperty(value = "计费方案ID")
    private Long chargingId;

    /**
     * 类型
     */
    @ExcelProperty(value = "类型")
    private String type;

    /**
     * 开始时间
     */
    @ExcelProperty(value = "开始时间")
    private Date startDate;

    /**
     * 结束时间
     */
    @ExcelProperty(value = "结束时间")
    private Date endDate;

    /**
     * 单价
     */
    @ExcelProperty(value = "单价")
    private BigDecimal unitPrice;

    /**
     * 尖峰平谷开关（0正常 1停用）
     */
    @ExcelProperty(value = "尖峰平谷开关", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String periodSwitch;

    /**
     * 尖时单价
     */
    @ExcelProperty(value = "尖时单价")
    private BigDecimal sharpPrice;

    /**
     * 峰时单价
     */
    @ExcelProperty(value = "峰时单价")
    private BigDecimal peekPrice;

    /**
     * 平时单价
     */
    @ExcelProperty(value = "平时单价")
    private BigDecimal ordinaryPrice;

    /**
     * 谷时单价
     */
    @ExcelProperty(value = "谷时单价")
    private BigDecimal valleyPrice;

    /**
     * 阶梯计费开关（0正常 1停用）
     */
    @ExcelProperty(value = "阶梯计费开关", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String stepSwitch;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
