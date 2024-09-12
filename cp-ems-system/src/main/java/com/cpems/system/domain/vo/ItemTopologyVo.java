package com.cpems.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.cpems.common.annotation.ExcelDictFormat;
import com.cpems.common.convert.ExcelDictConvert;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


/**
 * 项目拓扑视图对象 item_topology
 *
 * @author ruoyi
 * @date 2023-04-06
 */
@Data
@ExcelIgnoreUnannotated
public class ItemTopologyVo {

    private static final long serialVersionUID = 1L;

    /**
     * 项目id
     */
    @ExcelProperty(value = "项目id")
    private Long itemId;

    /**
     * 父项目id
     */
    @ExcelProperty(value = "父项目id")
    private Long parentId;

    /**
     * 祖级列表
     */
    @ExcelProperty(value = "祖级列表")
    private String ancestors;

    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称")
    private String itemName;

    /**
     * 显示顺序
     */
    @ExcelProperty(value = "显示顺序")
    private Long orderNum;

    /**
     * 部门状态（0正常 1停用）
     */
    @ExcelProperty(value = "部门状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 项目类型
     */
    @ExcelProperty(value = "项目类型")
    private String itemType;

    /**
     * 能耗
     */
    @ExcelProperty(value = "能耗")
    private BigDecimal value;

    /**
     * 当前支路能耗(kW·h)
     */
    @ExcelProperty(value = "当前支路能耗(kW·h)")
    private BigDecimal currentConsumption;

    /**
     * 下级支路能耗合计(kW·h)
     */
    @ExcelProperty(value = "下级支路能耗合计(kW·h)")
    private BigDecimal lowerConsumption;

    /**
     * 上级支路和下级支路能耗合计的差值(kW·h)
     */
    @ExcelProperty(value = "上级支路和下级支路能耗合计的差值(kW·h)")
    private BigDecimal difference;

    /**
     * 相差百分比(%)
     */
    @ExcelProperty(value = "相差百分比(%)")
    private BigDecimal percentage;

    /**
     * 设备id
     */
    @ExcelProperty(value = "设备id")
    private String deviceId;

    /**
     * 绑定设备集合
     */
    private List<String> deviceIds;

}
