package com.cpems.system.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 环比数据视图
 *
 * @Author cpems
 * @Date 2023/6/28 17:05
 */
@Data
public class ChainDataVo {
    private static final long serialVersionUID=1L;

    /**
     * 设备
     */
    private String device;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 区域
     */
    private String area;

    /**
     * 区域ID
     */
    private String areaId;

    /**
     * 本期用能
     */
    private BigDecimal currentEnergy;

    /**
     * 同期用能
     */
    private BigDecimal sameEnergy;

    /**
     * 增加值
     */
    private BigDecimal addEnergy;

    /**
     * 环比
     */
    private BigDecimal chain;
}
