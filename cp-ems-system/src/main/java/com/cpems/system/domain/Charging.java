package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 计费方案信息对象 charging
 *
 * @author cpems
 * @date 2023-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("charging")
public class Charging extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 计费方案ID
     */
    @TableId(value = "charging_id")
    private Long chargingId;
    /**
     * 类型
     */
    private String type;
    /**
     * 开始时间
     */
    private Date startDate;
    /**
     * 结束时间
     */
    private Date endDate;
    /**
     * 单价
     */
    private BigDecimal unitPrice;
    /**
     * 尖峰平谷开关（0正常 1停用）
     */
    private String periodSwitch;
    /**
     * 尖时单价
     */
    private BigDecimal sharpPrice;
    /**
     * 峰时单价
     */
    private BigDecimal peekPrice;
    /**
     * 平时单价
     */
    private BigDecimal ordinaryPrice;
    /**
     * 谷时单价
     */
    private BigDecimal valleyPrice;
    /**
     * 阶梯计费开关（0正常 1停用）
     */
    private String stepSwitch;
    /**
     * 状态（0正常 1停用）
     */
    private String status;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;
    /**
     * 备注
     */
    private String remark;

}
