package com.cpems.system.domain.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.cpems.common.annotation.ExcelDictFormat;
import com.cpems.common.convert.ExcelDictConvert;
import lombok.Data;


/**
 * 定额配置视图对象 quota_config
 *
 * @author cpems
 * @date 2023-09-12
 */
@Data
@ExcelIgnoreUnannotated
public class QuotaConfigVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long quotaId;

    /**
     * 定额类型（0 月度，1 年度）
     */
    @ExcelProperty(value = "定额类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=,月=度，1,年=度")
    private String quotaType;

    /**
     * 区域
     */
    @ExcelProperty(value = "区域")
    private Long itemId;

    /**
     * 定额时间
     */
    @ExcelProperty(value = "定额时间")
    private Date quotaTime;

    /**
     * 定额数值
     */
    @ExcelProperty(value = "定额数值")
    private BigDecimal quotaValue;

    /**
     * 实际累计能耗
     */
    @ExcelProperty(value = "实际累计能耗")
    private BigDecimal realEnergy;

    /**
     * 临界范围
     */
    @ExcelProperty(value = "临界范围")
    private BigDecimal critical;

    /**
     * 越限范围
     */
    @ExcelProperty(value = "越限范围")
    private BigDecimal overMedian;

    /**
     * 区域名称
     */
    private String itemName;

    /**
     * 临界范围Vo
     */
    private String criticalVo;

    /**
     * 越限范围Vo
     */
    private String overMedianVo;

}
