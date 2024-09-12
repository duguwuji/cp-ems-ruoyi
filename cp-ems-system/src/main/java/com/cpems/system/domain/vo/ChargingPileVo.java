package com.cpems.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.cpems.common.annotation.ExcelDictFormat;
import com.cpems.common.convert.ExcelDictConvert;
import lombok.Data;


/**
 * 充电桩信息视图对象 charging_pile
 *
 * @author ruoyi
 * @date 2023-10-08
 */
@Data
@ExcelIgnoreUnannotated
public class ChargingPileVo {

    private static final long serialVersionUID = 1L;

    /**
     * 充电桩ID
     */
    @ExcelProperty(value = "充电桩ID")
    private Long pileId;

    /**
     * 终端编码
     */
    @ExcelProperty(value = "终端编码")
    private String encoding;

    /**
     * 终端类型
     */
    @ExcelProperty(value = "终端类型")
    private String type;

    /**
     * 终端名称
     */
    @ExcelProperty(value = "终端名称")
    private String name;

    /**
     * 归属商户
     */
    @ExcelProperty(value = "归属商户")
    private String merchantId;

    /**
     * 归属商户名
     */
    @ExcelProperty(value = "归属商户名")
    private String merchantName;

    /**
     * 归属电站
     */
    @ExcelProperty(value = "归属电站")
    private String stationId;

    /**
     * 归属电站名称
     */
    @ExcelProperty(value = "归属电站名称")
    private String stationName;

    /**
     * 品牌
     */
    @ExcelProperty(value = "品牌")
    private String brand;

    /**
     * 型号
     */
    @ExcelProperty(value = "型号")
    private String model;

    /**
     * 电桩状态（0正常 1停用）
     */
    @ExcelProperty(value = "电桩状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 工作状态
     */
    @ExcelProperty(value = "工作状态")
    private String workStatus;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
