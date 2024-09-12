package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

/**
 * 制度管理业务对象 regulation_info
 *
 * @author cpems
 * @date 2023-09-11
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class RegulationInfoBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long regulationId;

    /**
     * 制度编码
     */
    @NotBlank(message = "制度编码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String regulationCode;

    /**
     * 制度名称
     */
    @NotBlank(message = "制度名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String regulationName;

    /**
     * 制度描述
     */
//    @NotBlank(message = "制度描述不能为空", groups = { AddGroup.class, EditGroup.class })
    private String regulationDescribe;

    /**
     * 制度类型
     */
    @NotBlank(message = "制度类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String regulationType;

    /**
     * 制度文件
     */
//    @NotBlank(message = "制度文件不能为空", groups = { AddGroup.class, EditGroup.class })
    private String docOssId;

    /**
     * 上传时间
     */
//    @NotNull(message = "上传时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private String uploadTime;

    /**
     * 备注
     */
    private String remark;


}
