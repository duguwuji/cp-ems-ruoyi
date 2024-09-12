package com.cpems.system.domain.vo;

import java.math.BigDecimal;
import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.cpems.common.annotation.ExcelDictFormat;
import com.cpems.common.convert.ExcelDictConvert;
import lombok.Data;


/**
 * 功率统计视图对象 power_statistics
 *
 * @author ruoyi
 * @date 2023-09-08
 */
@Data
@ExcelIgnoreUnannotated
public class PowerStatisticsVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 设备编号
     */
    @ExcelProperty(value = "设备编号")
    private String equipmentSn;

    /**
     * 能源类型（0：电，1：水）
     */
    @ExcelProperty(value = "能源类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：电，1：水")
    private String energyType;

    /**
     * 时间
     */
    @ExcelProperty(value = "时间")
    private Date time;

    /**
     * 谷值
     */
    @ExcelProperty(value = "谷值")
    private BigDecimal min;

    /**
     * 峰值
     */
    @ExcelProperty(value = "峰值")
    private BigDecimal max;

    /**
     * 均值
     */
    @ExcelProperty(value = "均值")
    private BigDecimal ave;

    /**
     * 日期
     */
    @ExcelProperty(value = "日期")
    private String date;


}
