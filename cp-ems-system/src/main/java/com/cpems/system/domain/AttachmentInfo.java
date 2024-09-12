package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 备件库管理对象 attachment_info
 *
 * @author cpems
 * @date 2023-09-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("attachment_info")
public class AttachmentInfo extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "attachment_id")
    private Long attachmentId;
    /**
     * 备件编码
     */
    private String attachmentCode;
    /**
     * 备件名称
     */
    private String attachmentName;
    /**
     * 备件型号
     */
    private String model;
    /**
     * 备件数量
     */
    private Integer quantity;
    /**
     * 单位
     */
    private String unit;
    /**
     * 供应商id
     */
    private String purveyorId;
    /**
     * 供应商名称
     */
    private String purveyorName;
    /**
     * 备注
     */
    private String remark;

}

