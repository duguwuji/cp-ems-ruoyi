package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 型号信息业务对象 model
 *
 * @author cpems
 * @date 2023-10-11
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ModelBo extends BaseEntity {

    /**
     * 型号id
     */
//    @NotNull(message = "型号id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 型号名称
     */
    @NotBlank(message = "型号名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String modelName;

    /**
     * 品牌id
     */
    @NotNull(message = "品牌id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long brandId;

    /**
     * 型号状态
     */
    @NotBlank(message = "型号状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;


}
