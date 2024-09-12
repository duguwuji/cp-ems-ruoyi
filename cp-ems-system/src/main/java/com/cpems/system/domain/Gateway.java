package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 网关信息对象 gateway
 *
 * @author cpems
 * @date 2023-04-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gateway")
public class Gateway extends BaseEntity {

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
     * 网关SN
     */
    private String sn;
    /**
     * 网关型号
     */
    private String model;
    /**
     * 网关描述
     */
    private String description;
    /**
     * 设备时区
     */
    private String timeZone;
    /**
     * 网关图片
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
     * 区域id
     */
    private Long areaId;
    /**
     * 区域
     */
    private String area;

}
