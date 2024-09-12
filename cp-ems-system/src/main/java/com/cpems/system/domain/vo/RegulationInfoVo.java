package com.cpems.system.domain.vo;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 制度管理视图对象 regulation_info
 *
 * @author cpems
 * @date 2023-09-11
 */
@Data
@ExcelIgnoreUnannotated
public class RegulationInfoVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long regulationId;

    /**
     * 制度编码
     */
    @ExcelProperty(value = "制度编码")
    private String regulationCode;

    /**
     * 制度名称
     */
    @ExcelProperty(value = "制度名称")
    private String regulationName;

    /**
     * 制度描述
     */
    @ExcelProperty(value = "制度描述")
    private String regulationDescribe;

    /**
     * 制度类型
     */
    @ExcelProperty(value = "制度类型")
    private String regulationType;

    /**
     * 制度文件
     */
    @ExcelProperty(value = "制度文件")
    private String docOssId;

    /**
     * 上传时间
     */
    @ExcelProperty(value = "上传时间")
    private Date uploadTime;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
