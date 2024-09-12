package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.math.BigDecimal;

/**
 * 充电站信息业务对象 charging_station
 *
 * @author cpems
 * @date 2023-10-08
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ChargingStationBo extends BaseEntity {

    /**
     * 充电站ID
     */
    @NotNull(message = "充电站ID不能为空", groups = { EditGroup.class })
    private Long stationId;

    /**
     * 归属商户
     */
    @NotBlank(message = "归属商户不能为空", groups = { AddGroup.class, EditGroup.class })
    private String merchantId;

    /**
     * 归属商户名
     */
    @NotBlank(message = "归属商户名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String merchantName;

    /**
     * 充电站类型
     */
    @NotBlank(message = "充电站类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;

    /**
     * 充电站名称
     */
    @NotBlank(message = "充电站名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 电站电价
     */
    @NotNull(message = "电站电价不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal price;

    /**
     * 电站地址
     */
    @NotBlank(message = "电站地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String address;

    /**
     * 电站活动
     */
//    @NotBlank(message = "电站活动不能为空", groups = { AddGroup.class, EditGroup.class })
    private String activity;

    /**
     * 电站状态（0正常 1停用）
     */
//    @NotBlank(message = "电站状态（0正常 1停用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

    /**
     * 备注
     */
//    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
