package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 型号信息对象 model
 *
 * @author cpems
 * @date 2023-10-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("model")
public class Model extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 型号id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 型号名称
     */
    private String modelName;
    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 型号状态
     */
    private String status;

}
