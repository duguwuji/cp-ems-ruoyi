package com.cpems.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.cpems.common.annotation.ExcelDictFormat;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;


/**
 * 品牌信息视图对象 brand
 *
 * @author cpems
 * @date 2023-10-10
 */
@Data
@ExcelIgnoreUnannotated
public class BrandVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @ExcelProperty(value = "品牌id")
    private Long id;

    /**
     * 品牌名称
     */
    @ExcelProperty(value = "品牌名称")
    private String brandName;

    /**
     * 品牌状态
     */
    @ExcelProperty(value = "品牌状态")
    @ExcelDictFormat(dictType = "sys_normal_disable")
    private String status;


}
