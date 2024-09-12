package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 设备信息业务对象 equipment_info
 *
 * @author ruoyi
 * @date 2023-04-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class EquipmentInfoBo extends BaseEntity {

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
     * 设备SN
     */
    @NotBlank(message = "设备SN不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sn;

    /**
     * 设备型号
     */
    @NotBlank(message = "设备型号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String model;

    /**
     * 设备描述
     */
//    @NotBlank(message = "设备描述不能为空", groups = { AddGroup.class, EditGroup.class })
    private String description;

    /**
     * 设备图片
     */
//    @NotBlank(message = "设备图片不能为空", groups = { AddGroup.class, EditGroup.class })
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
     * 区域
     */
    @NotBlank(message = "设备类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;
    /**
     * 设备状态
     */
//    @NotBlank(message = "设备状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

}
