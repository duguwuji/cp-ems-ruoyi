package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 商户信息对象 merchant
 *
 * @author cpems
 * @date 2023-10-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("merchant")
public class Merchant extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 商户ID
     */
    @TableId(value = "merchant_id")
    private Long merchantId;
    /**
     * 商户名称
     */
    private String name;
    /**
     * 商户类型
     */
    private String type;
    /**
     * 联系方式
     */
    private String contact;
    /**
     * 商户地址
     */
    private String avatar;
    /**
     * 商户状态（0正常 1停用）
     */
    private String status;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;
    /**
     * 备注
     */
    private String remark;

}
