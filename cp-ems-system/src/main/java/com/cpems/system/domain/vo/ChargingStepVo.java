package com.cpems.system.domain.vo;

import java.math.BigDecimal;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 阶梯计费参数信息视图对象 charging_step
 *
 * @author ruoyi
 * @date 2023-10-31
 */
@Data
@ExcelIgnoreUnannotated
public class ChargingStepVo {

    private static final long serialVersionUID = 1L;

    /**
     * 阶梯计费id
     */
    @ExcelProperty(value = "阶梯计费id")
    private Long id;

    /**
     * 计费方案id
     */
    @ExcelProperty(value = "计费方案id")
    private Long chargingId;

    /**
     * 开始用量
     */
    @ExcelProperty(value = "开始用量")
    private Integer startStep;

    /**
     * 结束用量
     */
    @ExcelProperty(value = "结束用量")
    private Integer endStep;

    /**
     * 差价
     */
    @ExcelProperty(value = "差价")
    private BigDecimal priceDifference;


}
