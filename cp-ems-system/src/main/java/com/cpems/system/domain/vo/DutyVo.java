package com.cpems.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 值班管理视图对象 duty
 *
 * @author ruoyi
 * @date 2023-09-11
 */
@Data
@ExcelIgnoreUnannotated
public class DutyVo {

    private static final long serialVersionUID = 1L;

    /**
     * 值班id
     */
    @ExcelProperty(value = "值班id")
    private Long dutyId;

    /**
     * 值班日期
     */
    @ExcelProperty(value = "值班日期")
    private String dutyDate;

    /**
     * 值班人员
     */
    @ExcelProperty(value = "值班人员")
    private String onDuty;

    /**
     * 值班时间段
     */
    @ExcelProperty(value = "值班时间段")
    private String dutyPeriod;

    /**
     * 值班类型
     */
    @ExcelProperty(value = "值班类型")
    private String type;

    /**
     * 值班状态
     */
    @ExcelProperty(value = "值班状态")
    private String status;

    /**
     * 替班人员
     */
    @ExcelProperty(value = "替班人员")
    private String relief;

    /**
     * 值班备注
     */
    @ExcelProperty(value = "值班备注")
    private String remark;


}
