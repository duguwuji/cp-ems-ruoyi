package com.cpems.system.domain.vo;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 流程管理视图对象 process_info
 *
 * @author cpems
 * @date 2023-09-11
 */
@Data
@ExcelIgnoreUnannotated
public class ProcessInfoVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long processId;

    /**
     * 事件名称
     */
    @ExcelProperty(value = "事件名称")
    private String eventName;

    /**
     * 事件类型
     */
    @ExcelProperty(value = "事件类型")
    private String eventType;

    /**
     * 触发时间
     */
    @ExcelProperty(value = "触发时间")
    private Date touchTime;

    /**
     * 处理结果
     */
    @ExcelProperty(value = "处理结果")
    private String handleResult;

    /**
     * 处理人
     */
    @ExcelProperty(value = "处理人")
    private Long handlePerson;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
