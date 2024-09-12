package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 巡检记录对象 inspection_record
 *
 * @author cpems
 * @date 2023-04-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inspection_record")
public class InspectionRecord extends BaseEntity {

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
     * 计划巡检时间
     */
    private Date planTime;
    /**
     * 实际巡检时间
     */
    private Date inspectionTime;
    /**
     * 巡检人
     */
    private String inspectionPerson;
    /**
     * 巡检状态
     */
    private String inspectionStatus;
    /**
     * 巡检备注
     */
    private String inspectionRemark;
    /**
     * 巡检人id
     */
    private String userId;
    /**
     * 周期
     */
    private String cycle;
    /**
     * 巡检周期
     */
    private String inspectionCycle;
    /**
     * 计划id
     */
    private Long planId;

}
