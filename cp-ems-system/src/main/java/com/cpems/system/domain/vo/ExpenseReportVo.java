package com.cpems.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ExcelIgnoreUnannotated
public class ExpenseReportVo {
    private static final long serialVersionUID = 1L;

    /**
     * 能源节点
     */
    @ExcelProperty(value = "能源节点")
    private String node;

    /**
     * 日期
     */
    @ExcelProperty(value = "日期")
    private String date;

    /**
     * 用能
     */
    @ExcelProperty(value = "用能")
    private BigDecimal energy;

    /**
     * 费用
     */
    @ExcelProperty(value = "费用")
    private BigDecimal cost;


    private List<ExpenseReportVo> list;

    /**
     * 总用能
     */
    @ExcelProperty(value = "总用能")
    private BigDecimal totalEnergy;

    /**
     * 总费用
     */
    @ExcelProperty(value = "总费用")
    private BigDecimal totalCost;



}
