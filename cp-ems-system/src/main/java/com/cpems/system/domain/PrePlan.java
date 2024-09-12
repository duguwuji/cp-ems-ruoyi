package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 预案管理对象 pre_plan
 *
 * @author cpems
 * @date 2023-09-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pre_plan")
public class PrePlan extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 预案id
     */
    @TableId(value = "pre_plan_id")
    private Long prePlanId;
    /**
     * 预案编号
     */
    private String number;
    /**
     * 预案类型
     */
    private String type;
    /**
     * 站名
     */
    private String station;
    /**
     * 设备
     */
    private String device;
    /**
     * 预案内容
     */
    private String content;
    /**
     * 编制人
     */
    private String preparedBy;
    /**
     * 预案关键词
     */
    private String keywords;

}
