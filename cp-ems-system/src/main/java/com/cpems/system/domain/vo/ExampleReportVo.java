package com.cpems.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 例报管理视图对象 example_report
 *
 * @author ruoyi
 * @date 2023-09-11
 */
@Data
@ExcelIgnoreUnannotated
public class ExampleReportVo {

    private static final long serialVersionUID = 1L;

    /**
     * 例报id
     */
    @ExcelProperty(value = "例报id")
    private Long exampleReportId;

    /**
     * 例报类型
     */
    @ExcelProperty(value = "例报类型")
    private String type;

    /**
     * 例报周期
     */
    @ExcelProperty(value = "例报周期")
    private String cycle;

    /**
     * 例报内容
     */
    @ExcelProperty(value = "例报内容")
    private String content;

    /**
     * 推送方式
     */
    @ExcelProperty(value = "推送方式")
    private String pushMethod;

    /**
     * 接收人id
     */
    @ExcelProperty(value = "接收人id")
    private String userId;

    /**
     * 接收人
     */
    @ExcelProperty(value = "接收人")
    private String receiver;

    /**
     * 开始日期
     */
    @ExcelProperty(value = "开始日期")
    private Date startDate;

    /**
     * 结束日期
     */
    @ExcelProperty(value = "结束日期")
    private Date endDate;


}
