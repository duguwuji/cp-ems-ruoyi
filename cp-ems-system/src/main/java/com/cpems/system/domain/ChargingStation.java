package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 充电站信息对象 charging_station
 *
 * @author cpems
 * @date 2023-10-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("charging_station")
public class ChargingStation extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 充电站ID
     */
    @TableId(value = "station_id")
    private Long stationId;
    /**
     * 归属商户
     */
    private String merchantId;
    /**
     * 归属商户名
     */
    private String merchantName;
    /**
     * 充电站类型
     */
    private String type;
    /**
     * 充电站名称
     */
    private String name;
    /**
     * 电站电价
     */
    private BigDecimal price;
    /**
     * 电站地址
     */
    private String address;
    /**
     * 电站活动
     */
    private String activity;
    /**
     * 电站状态（0正常 1停用）
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
