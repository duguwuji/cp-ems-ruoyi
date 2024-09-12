package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 作业规范对象 standard_info
 *
 * @author cpems
 * @date 2023-09-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("standard_info")
public class StandardInfo extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "standard_id")
    private Long standardId;
    /**
     * 规范编码
     */
    private String standardCode;
    /**
     * 规范名称
     */
    private String standardName;
    /**
     * 规范描述
     */
    private String standardDescribe;
    /**
     * 规范文件
     */
    private String docOssId;
    /**
     * 备注
     */
    private String remark;

}
