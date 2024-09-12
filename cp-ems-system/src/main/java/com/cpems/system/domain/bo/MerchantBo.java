package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 商户信息业务对象 merchant
 *
 * @author ruoyi
 * @date 2023-10-07
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class MerchantBo extends BaseEntity {

    /**
     * 商户ID
     */
    @NotNull(message = "商户ID不能为空", groups = { EditGroup.class })
    private Long merchantId;

    /**
     * 商户名称
     */
    @NotBlank(message = "商户名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 商户类型
     */
    @NotBlank(message = "商户类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;

    /**
     * 联系方式
     */
    @NotBlank(message = "联系方式不能为空", groups = { AddGroup.class, EditGroup.class })
    private String contact;

    /**
     * 商户地址
     */
    @NotBlank(message = "商户地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String avatar;

    /**
     * 商户状态（0正常 1停用）
     */
//    @NotBlank(message = "商户状态（0正常 1停用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

    /**
     * 备注
     */
//    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
