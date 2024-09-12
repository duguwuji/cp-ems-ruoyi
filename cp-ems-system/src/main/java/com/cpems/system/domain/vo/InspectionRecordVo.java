package com.cpems.system.domain.vo;

import java.util.Date;
import java.util.List;

import com.cpems.common.core.domain.BaseEntity;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 巡检记录视图对象 inspection_record
 *
 * @author ruoyi
 * @date 2023-04-04
 */
@Data
@ExcelIgnoreUnannotated
public class InspectionRecordVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 计划名称
     */
    @ExcelProperty(value = "计划名称")
    private String planName;

    /**
     * 计划内容
     */
    @ExcelProperty(value = "计划内容")
    private String planContent;

    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称")
    private String projectName;

    /**
     * 计划巡检时间
     */
    @ExcelProperty(value = "计划巡检时间")
    private Date planTime;

    /**
     * 实际巡检时间
     */
    @ExcelProperty(value = "实际巡检时间")
    private Date inspectionTime;

    /**
     * 巡检人
     */
    @ExcelProperty(value = "巡检人")
    private String inspectionPerson;

    /**
     * 巡检状态
     */
    @ExcelProperty(value = "巡检状态")
    private String inspectionStatus;

    /**
     * 巡检备注
     */
    @ExcelProperty(value = "巡检备注")
    private String inspectionRemark;

    /**
     * 巡检人id
     */
    @ExcelProperty(value = "巡检人id")
    private String userId;

    /**
     * 巡检人ids
     */
    @ExcelProperty(value = "巡检人ids")
    private List<String> userIds;

    /**
     * 周期
     */
    @ExcelProperty(value = "周期")
    private String cycle;

    /**
     * 巡检周期
     */
    @ExcelProperty(value = "巡检周期不能为空")
    private String inspectionCycle;

    /**
     * 计划id
     */
    @ExcelProperty(value = "计划id")
    private Long planId;

}
