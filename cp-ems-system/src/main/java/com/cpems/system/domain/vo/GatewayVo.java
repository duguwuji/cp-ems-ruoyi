package com.cpems.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 网关信息视图对象 gateway
 *
 * @author ruoyi
 * @date 2023-04-10
 */
@Data
@ExcelIgnoreUnannotated
public class GatewayVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String name;

    /**
     * 网关SN
     */
    @ExcelProperty(value = "网关SN")
    private String sn;

    /**
     * 网关型号
     */
    @ExcelProperty(value = "网关型号")
    private String model;

    /**
     * 网关描述
     */
    @ExcelProperty(value = "网关描述")
    private String description;

    /**
     * 设备时区
     */
    @ExcelProperty(value = "设备时区")
    private String timeZone;

    /**
     * 网关图片
     */
    @ExcelProperty(value = "网关图片")
    private String img;

    /**
     * 设备二维码
     */
    @ExcelProperty(value = "设备二维码")
    private String qrCode;

    /**
     * 工厂
     */
    @ExcelProperty(value = "工厂")
    private String factory;

    /**
     * 区域
     */
    @ExcelProperty(value = "区域")
    private String area;

    /**
     * 区域id
     */
    @ExcelProperty(value = "区域id")
    private Long areaId;


}
