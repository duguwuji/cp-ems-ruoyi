package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 设备信息对象 equipment_info
 *
 * @author cpems
 * @date 2023-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("equipment_info")
public class EquipmentInfo extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 设备SN
     */
    private String sn;
    /**
     * 设备型号
     */
    private String model;
    /**
     * 设备描述
     */
    private String description;
    /**
     * 设备图片
     */
    private String img;
    /**
     * 设备二维码
     */
    private String qrCode;
    /**
     * 工厂
     */
    private String factory;
    /**
     * 设备类型
     */
    private String type;
    /**
     * 设备状态
     */
    private String status;


}
