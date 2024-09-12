package com.cpems.system.domain.vo;

import java.math.BigDecimal;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.cpems.common.annotation.ExcelDictFormat;
import com.cpems.common.convert.ExcelDictConvert;
import lombok.Data;


/**
 * 充电站信息视图对象 charging_station
 *
 * @author ruoyi
 * @date 2023-10-08
 */
@Data
@ExcelIgnoreUnannotated
public class ChargingStationVo {

    private static final long serialVersionUID = 1L;

    /**
     * 充电站ID
     */
    @ExcelProperty(value = "充电站ID")
    private Long stationId;

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
     * 充电站类型
     */
    @ExcelProperty(value = "充电站类型")
    private String type;

    /**
     * 充电站名称
     */
    @ExcelProperty(value = "充电站名称")
    private String name;

    /**
     * 电站电价
     */
    @ExcelProperty(value = "电站电价")
    private BigDecimal price;

    /**
     * 电站地址
     */
    @ExcelProperty(value = "电站地址")
    private String address;

    /**
     * 电站活动
     */
    @ExcelProperty(value = "电站活动")
    private String activity;

    /**
     * 电站状态（0正常 1停用）
     */
    @ExcelProperty(value = "电站状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
