package com.cpems.system.domain.vo;

import java.util.Date;
import java.util.List;

import com.cpems.common.core.domain.BaseEntity;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.cpems.common.annotation.ExcelDictFormat;
import com.cpems.common.convert.ExcelDictConvert;
import lombok.Data;


/**
 * 巡检计划视图对象 inspection_plan
 *
 * @author ruoyi
 * @date 2023-04-04
 */
@Data
@ExcelIgnoreUnannotated
public class InspectionPlanVo extends BaseEntity {

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
     * 开始日期
     */
    @ExcelProperty(value = "开始日期")
    private Date startDate;

    /**
     * 结束日期
     */
    @ExcelProperty(value = "结束日期")
    private Date endDate;

    /**
     * 巡检周期
     */
    @ExcelProperty(value = "巡检周期")
    private String inspectionCycle;

    /**
     * 巡检人
     */
    @ExcelProperty(value = "巡检人")
    private String inspectionPerson;

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
     * 巡检日期
     */
    @ExcelProperty(value = "巡检日期")
    private String setDate;

    /**
     * 巡检日期s
     */
    @ExcelProperty(value = "巡检日期s")
    private List<String> setDates;

    /**
     * 巡检固定时间
     */
    @ExcelProperty(value = "巡检固定时间")
    private String setTime;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

}
