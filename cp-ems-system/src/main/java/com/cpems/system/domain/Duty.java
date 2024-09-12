package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 值班管理对象 duty
 *
 * @author cpems
 * @date 2023-09-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("duty")
public class Duty extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 值班id
     */
    @TableId(value = "duty_id")
    private Long dutyId;
    /**
     * 值班日期
     */
    private String dutyDate;
    /**
     * 值班人员
     */
    private String onDuty;
    /**
     * 值班时间段
     */
    private String dutyPeriod;
    /**
     * 值班类型
     */
    private String type;
    /**
     * 值班状态
     */
    private String status;
    /**
     * 替班人员
     */
    private String relief;
    /**
     * 值班备注
     */
    private String remark;

}
