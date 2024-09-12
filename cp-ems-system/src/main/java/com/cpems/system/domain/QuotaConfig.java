package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 定额配置对象 quota_config
 *
 * @author cpems
 * @date 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("quota_config")
public class QuotaConfig extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "quota_id")
    private Long quotaId;
    /**
     * 定额类型（0 月度，1 年度）
     */
    private String quotaType;
    /**
     * 区域
     */
    private Long itemId;
    /**
     * 定额时间
     */
    private Date quotaTime;
    /**
     * 定额数值
     */
    private BigDecimal quotaValue;
    /**
     * 实际累计能耗
     */
    private BigDecimal realEnergy;
    /**
     * 临界范围
     */
    private BigDecimal critical;
    /**
     * 越限范围
     */
    private BigDecimal overMedian;

}
