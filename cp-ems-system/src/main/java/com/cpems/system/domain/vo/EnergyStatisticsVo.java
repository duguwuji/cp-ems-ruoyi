package com.cpems.system.domain.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.cpems.common.core.domain.BaseEntity;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.cpems.common.annotation.ExcelDictFormat;
import com.cpems.common.convert.ExcelDictConvert;
import lombok.Data;


/**
 * 能耗统计视图对象 energy_statistics
 *
 * @author ruoyi
 * @date 2023-08-24
 */
@Data
@ExcelIgnoreUnannotated
public class EnergyStatisticsVo extends BaseEntity{

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
    @ExcelDictFormat(readConverterExp = "0=电，1=水")
    private String energyType;

    /**
     * 时间
     */
    @ExcelProperty(value = "时间")
    private Date time;

    /**
     * 能耗
     */
    @ExcelProperty(value = "能耗")
    private BigDecimal statistics;


}

