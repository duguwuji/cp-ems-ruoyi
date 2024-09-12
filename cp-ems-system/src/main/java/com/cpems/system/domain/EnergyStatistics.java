package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 能耗统计对象 energy_statistics
 *
 * @author cpems
 * @date 2023-08-24
 */
@Data
@TableName("energy_statistics")
public class EnergyStatistics extends BaseEntity{

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
     * 能耗
     */
    private BigDecimal statistics;

}

