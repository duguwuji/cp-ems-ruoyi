package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 品牌信息对象 brand
 *
 * @author cpems
 * @date 2023-10-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("brand")
public class Brand extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 品牌id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 品牌状态
     */
    private String status;

}
