package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 巡检计划对象 inspection_plan
 *
 * @author cpems
 * @date 2023-04-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inspection_plan")
public class InspectionPlan extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 计划名称
     */
    private String planName;
    /**
     * 计划内容
     */
    private String planContent;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 开始日期
     */
    private Date startDate;
    /**
     * 结束日期
     */
    private Date endDate;
    /**
     * 巡检周期
     */
    private String inspectionCycle;
    /**
     * 巡检人
     */
    private String inspectionPerson;
    /**
     * 巡检人id
     */
    private String userId;
    /**
     * 巡检日期
     */
    private String setDate;
    /**
     * 巡检固定时间
     */
    private String setTime;
    /**
     * 状态（0正常 1停用）
     */
    private String status;

}
