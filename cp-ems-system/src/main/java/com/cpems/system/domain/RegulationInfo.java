package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 制度管理对象 regulation_info
 *
 * @author cpems
 * @date 2023-09-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("regulation_info")
public class RegulationInfo extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "regulation_id")
    private Long regulationId;
    /**
     * 制度编码
     */
    private String regulationCode;
    /**
     * 制度名称
     */
    private String regulationName;
    /**
     * 制度描述
     */
    private String regulationDescribe;
    /**
     * 制度类型
     */
    private String regulationType;
    /**
     * 制度文件
     */
    private String docOssId;
    /**
     * 上传时间
     */
    private Date uploadTime;
    /**
     * 备注
     */
    private String remark;

}
