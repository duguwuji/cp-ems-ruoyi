package com.cpems.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 预案管理视图对象 pre_plan
 *
 * @author ruoyi
 * @date 2023-09-11
 */
@Data
@ExcelIgnoreUnannotated
public class PrePlanVo {

    private static final long serialVersionUID = 1L;

    /**
     * 预案id
     */
    @ExcelProperty(value = "预案id")
    private Long prePlanId;

    /**
     * 预案编号
     */
    @ExcelProperty(value = "预案编号")
    private String number;

    /**
     * 预案类型
     */
    @ExcelProperty(value = "预案类型")
    private String type;

    /**
     * 站名
     */
    @ExcelProperty(value = "站名")
    private String station;

    /**
     * 设备
     */
    @ExcelProperty(value = "设备")
    private String device;

    /**
     * 预案内容
     */
    @ExcelProperty(value = "预案内容")
    private String content;

    /**
     * 编制人
     */
    @ExcelProperty(value = "编制人")
    private String preparedBy;

    /**
     * 预案关键词
     */
    @ExcelProperty(value = "预案关键词")
    private String keywords;


}
