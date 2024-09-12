package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;
import java.util.List;


/**
 * 项目拓扑业务对象 item_topology
 *
 * @author cpems
 * @date 2023-04-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ItemTopologyBo extends BaseEntity {

    /**
     * 项目id
     */
    @NotNull(message = "项目id不能为空", groups = { EditGroup.class })
    private Long itemId;

    /**
     * 父项目id
     */
    @NotNull(message = "父项目id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long parentId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String itemName;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long orderNum;

    /**
     * 部门状态（0正常 1停用）
     */
    @NotBlank(message = "部门状态（0正常 1停用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

    /**
     * 项目类型
     */
    @NotBlank(message = "项目类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String itemType;

    /**
     * 设备id
     */
//    @NotBlank(message = "设备id", groups = { AddGroup.class, EditGroup.class })
    private String deviceId;

    /**
     * 绑定设备集合
     */
    private List<String> deviceIds;


}
