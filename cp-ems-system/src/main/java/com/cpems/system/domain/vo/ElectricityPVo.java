package com.cpems.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 电功率值视图对象 electricity_p
 *
 * @author cpems
 * @date 2023-04-21
 */
@Data
@ExcelIgnoreUnannotated
public class ElectricityPVo {

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
     * 电功率值
     */
    @ExcelProperty(value = "电功率值")
    private BigDecimal value;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

}
