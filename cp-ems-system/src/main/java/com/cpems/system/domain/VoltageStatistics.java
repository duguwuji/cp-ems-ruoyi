package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 电压统计对象 voltage_statistics
 *
 * @author cpems
 * @date 2023-10-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("voltage_statistics")
public class VoltageStatistics extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 设备编号
     */
    private String equipmentSn;
    /**
     * 能源类型（0：电，1：水）
     */
    private String energyType;
    /**
     * 时间
     */
    private Date time;
    /**
     * 谷值
     */
    private BigDecimal min;
    /**
     * 均值
     */
    private BigDecimal ave;
    /**
     * 峰值
     */
    private BigDecimal max;

}
