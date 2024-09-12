package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 流程管理对象 process_info
 *
 * @author cpems
 * @date 2023-09-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("process_info")
public class ProcessInfo extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "process_id")
    private Long processId;
    /**
     * 事件名称
     */
    private String eventName;
    /**
     * 事件类型
     */
    private String eventType;
    /**
     * 触发时间
     */
    private Date touchTime;
    /**
     * 处理结果
     */
    private String handleResult;
    /**
     * 处理人
     */
    private Long handlePerson;
    /**
     * 备注
     */
    private String remark;

}
