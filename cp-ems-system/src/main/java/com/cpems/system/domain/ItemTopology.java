package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 项目拓扑对象 item_topology
 *
 * @author cpems
 * @date 2023-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("item_topology")
public class ItemTopology extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 项目id
     */
    @TableId(value = "item_id")
    private Long itemId;
    /**
     * 父项目id
     */
    private Long parentId;
    /**
     * 祖级列表
     */
    private String ancestors;
    /**
     * 项目名称
     */
    private String itemName;
    /**
     * 显示顺序
     */
    private Long orderNum;
    /**
     * 部门状态（0正常 1停用）
     */
    private String status;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;
    /**
     * 项目类型
     */
    private String itemType;
    /**
     * 设备id
     */
    private String deviceId;

}
