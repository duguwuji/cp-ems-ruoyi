package com.cpems.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.sql.Timestamp;


/**
 * 能源超级表  energy
 *
 * @author cpems
 * @date 2023-06-19
 */
@Data
@ExcelIgnoreUnannotated
public class EnergyVo {

    private static final long serialVersionUID = 1L;

    /**
     * 时间戳
     */
    @ExcelProperty(value = "时间")
    private Timestamp ts;

    /**
     * 设备id
     */
    @ExcelProperty(value = "设备id")
    private String clientId;

    /**
     * 能源值
     */
    @ExcelProperty(value = "时间")
    private float val;

    /**
     * TAGS：类型
     */
    @ExcelProperty(value = "时间")
    private String type;

}
