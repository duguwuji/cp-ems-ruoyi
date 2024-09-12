package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 充电桩信息对象 charging_pile
 *
 * @author cpems
 * @date 2023-10-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("charging_pile")
public class ChargingPile extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 充电桩ID
     */
    @TableId(value = "pile_id")
    private Long pileId;
    /**
     * 终端编码
     */
    private String encoding;
    /**
     * 终端类型
     */
    private String type;
    /**
     * 终端名称
     */
    private String name;
    /**
     * 归属商户
     */
    private String merchantId;
    /**
     * 归属商户名
     */
    private String merchantName;
    /**
     * 归属电站
     */
    private String stationId;
    /**
     * 归属电站名称
     */
    private String stationName;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 型号
     */
    private String model;
    /**
     * 电桩状态（0正常 1停用）
     */
    private String status;
    /**
     * 工作状态
     */
    private String workStatus;
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
