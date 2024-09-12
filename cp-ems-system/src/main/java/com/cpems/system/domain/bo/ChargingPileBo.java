package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 充电桩信息业务对象 charging_pile
 *
 * @author cpems
 * @date 2023-10-08
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ChargingPileBo extends BaseEntity {

    /**
     * 充电桩ID
     */
    @NotNull(message = "充电桩ID不能为空", groups = { EditGroup.class })
    private Long pileId;

    /**
     * 终端编码
     */
    @NotBlank(message = "终端编码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String encoding;

    /**
     * 终端类型
     */
    @NotBlank(message = "终端类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;

    /**
     * 终端名称
     */
    @NotBlank(message = "终端名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

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
     * 归属电站
     */
    @NotBlank(message = "归属电站不能为空", groups = { AddGroup.class, EditGroup.class })
    private String stationId;

    /**
     * 归属电站名称
     */
    @NotBlank(message = "归属电站名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String stationName;

    /**
     * 品牌
     */
    @NotBlank(message = "品牌不能为空", groups = { AddGroup.class, EditGroup.class })
    private String brand;

    /**
     * 型号
     */
    @NotBlank(message = "型号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String model;

    /**
     * 电桩状态（0正常 1停用）
     */
//    @NotBlank(message = "电桩状态（0正常 1停用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

    /**
     * 工作状态
     */
//    @NotBlank(message = "工作状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String workStatus;

    /**
     * 备注
     */
//    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
