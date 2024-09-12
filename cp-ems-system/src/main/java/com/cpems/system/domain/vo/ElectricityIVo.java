package com.cpems.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 电流值视图对象 electricity_i
 *
 * @author cpems
 * @date 2023-04-21
 */
@Data
@ExcelIgnoreUnannotated
public class ElectricityIVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 设备id
     */
    @ExcelProperty(value = "设备id")
    private String clientId;

    /**
     * 电流值
     */
    @ExcelProperty(value = "电流值")
    private BigDecimal value;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

}
