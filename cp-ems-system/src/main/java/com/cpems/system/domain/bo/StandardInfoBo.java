package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 作业规范业务对象 standard_info
 *
 * @author cpems
 * @date 2023-09-11
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class StandardInfoBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long standardId;

    /**
     * 规范编码
     */
    @NotBlank(message = "规范编码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String standardCode;

    /**
     * 规范名称
     */
    @NotBlank(message = "规范名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String standardName;

    /**
     * 规范描述
     */
//    @NotBlank(message = "规范描述不能为空", groups = { AddGroup.class, EditGroup.class })
    private String standardDescribe;

    /**
     * 规范文件
     */
//    @NotBlank(message = "规范文件不能为空", groups = { AddGroup.class, EditGroup.class })
    private String docOssId;

    /**
     * 备注
     */
    private String remark;


}
