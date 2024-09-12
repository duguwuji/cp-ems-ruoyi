package com.cpems.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 作业规范视图对象 standard_info
 *
 * @author cpems
 * @date 2023-09-11
 */
@Data
@ExcelIgnoreUnannotated
public class StandardInfoVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long standardId;

    /**
     * 规范编码
     */
    @ExcelProperty(value = "规范编码")
    private String standardCode;

    /**
     * 规范名称
     */
    @ExcelProperty(value = "规范名称")
    private String standardName;

    /**
     * 规范描述
     */
    @ExcelProperty(value = "规范描述")
    private String standardDescribe;

    /**
     * 规范文件
     */
    @ExcelProperty(value = "规范文件")
    private String docOssId;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
