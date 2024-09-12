package com.cpems.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.cpems.common.annotation.ExcelDictFormat;
import com.cpems.common.convert.ExcelDictConvert;
import lombok.Data;


/**
 * 商户信息视图对象 merchant
 *
 * @author ruoyi
 * @date 2023-10-07
 */
@Data
@ExcelIgnoreUnannotated
public class MerchantVo {

    private static final long serialVersionUID = 1L;

    /**
     * 商户ID
     */
    @ExcelProperty(value = "商户ID")
    private Long merchantId;

    /**
     * 商户名称
     */
    @ExcelProperty(value = "商户名称")
    private String name;

    /**
     * 商户类型
     */
    @ExcelProperty(value = "商户类型")
    private String type;

    /**
     * 联系方式
     */
    @ExcelProperty(value = "联系方式")
    private String contact;

    /**
     * 商户地址
     */
    @ExcelProperty(value = "商户地址")
    private String avatar;

    /**
     * 商户状态（0正常 1停用）
     */
    @ExcelProperty(value = "商户状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
