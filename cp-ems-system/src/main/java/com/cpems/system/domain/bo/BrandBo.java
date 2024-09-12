package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 品牌信息业务对象 brand
 *
 * @author cpems
 * @date 2023-10-10
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class BrandBo extends BaseEntity {

    /**
     * 品牌id
     */
//    @NotNull(message = "品牌id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 品牌名称
     */
    @NotBlank(message = "品牌名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String brandName;

    /**
     * 品牌状态
     */
    @NotBlank(message = "品牌状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;


}
