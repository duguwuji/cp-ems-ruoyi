package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 网关信息业务对象 gateway
 *
 * @author ruoyi
 * @date 2023-04-10
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class GatewayBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 网关SN
     */
    @NotBlank(message = "网关SN不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sn;

    /**
     * 网关型号
     */
    @NotBlank(message = "网关型号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String model;

    /**
     * 网关描述
     */
//    @NotBlank(message = "网关描述不能为空", groups = { AddGroup.class, EditGroup.class })
    private String description;

    /**
     * 设备时区
     */
//    @NotBlank(message = "设备时区不能为空", groups = { AddGroup.class, EditGroup.class })
    private String timeZone;

    /**
     * 网关图片
     */
//    @NotBlank(message = "网关图片不能为空", groups = { AddGroup.class, EditGroup.class })
    private String img;

    /**
     * 设备二维码
     */
//    @NotBlank(message = "设备二维码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String qrCode;

    /**
     * 工厂
     */
//    @NotBlank(message = "工厂不能为空", groups = { AddGroup.class, EditGroup.class })
    private String factory;

    /**
     * 区域id
     */
//    @NotNull(message = "区域id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long areaId;

    /**
     * 区域
     */
//    @NotBlank(message = "区域不能为空", groups = { AddGroup.class, EditGroup.class })
    private String area;


}
