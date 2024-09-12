package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

/**
 * 流程管理业务对象 process_info
 *
 * @author cpems
 * @date 2023-09-11
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ProcessInfoBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long processId;

    /**
     * 事件名称
     */
    @NotBlank(message = "事件名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String eventName;

    /**
     * 事件类型
     */
    @NotBlank(message = "事件类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String eventType;

    /**
     * 触发时间
     */
    @NotNull(message = "触发时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private String touchTime;

    /**
     * 处理结果
     */
//    @NotBlank(message = "处理结果不能为空", groups = { AddGroup.class, EditGroup.class })
    private String handleResult;

    /**
     * 处理人
     */
//    @NotNull(message = "处理人不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long handlePerson;

    /**
     * 备注
     */
    private String remark;


}
