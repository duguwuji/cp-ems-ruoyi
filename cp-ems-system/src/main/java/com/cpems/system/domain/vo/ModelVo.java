package com.cpems.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.cpems.common.annotation.ExcelDictFormat;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;


/**
 * 型号信息视图对象 model
 *
 * @author cpems
 * @date 2023-10-11
 */
@Data
@ExcelIgnoreUnannotated
public class ModelVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 型号id
     */
    @ExcelProperty(value = "型号id")
    private Long id;

    /**
     * 型号名称
     */
    @ExcelProperty(value = "型号名称")
    private String modelName;

    /**
     * 品牌id
     */
    @ExcelProperty(value = "品牌id")
    private Long brandId;

    /**
     * 型号状态
     */
    @ExcelProperty(value = "型号状态")
    @ExcelDictFormat(dictType = "sys_normal_disable")
    private String status;


}
