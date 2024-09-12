package com.cpems.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.List;


/**
 * 设备信息视图对象 equipment_info
 *
 * @author ruoyi
 * @date 2023-04-06
 */
@Data
@ExcelIgnoreUnannotated
public class EquipmentInfoVo {

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
     * 设备SN
     */
    @ExcelProperty(value = "设备SN")
    private String sn;

    /**
     * 设备型号
     */
    @ExcelProperty(value = "设备型号")
    private String model;

    /**
     * 设备描述
     */
    @ExcelProperty(value = "设备描述")
    private String description;

    /**
     * 设备图片
     */
    @ExcelProperty(value = "设备图片")
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
    @ExcelProperty(value = "设备类型")
    private String type;
    /**
     * 设备状态
     */
    @ExcelProperty(value = "设备状态")
    private String status;
    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    private Long count;
    /**
     * 占比
     */
    @ExcelProperty(value = "占比")
    private String proportion;

    /**
     * 设备图片列表
     */
    private List<SysOssVo> imgOss;

    /**
     * 区域名称
     */
    private String areaName;

}
