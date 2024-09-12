package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 例报管理业务对象 example_report
 *
 * @author ruoyi
 * @date 2023-09-11
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ExampleReportBo extends BaseEntity {

    /**
     * 例报id
     */
//    @NotNull(message = "例报id不能为空", groups = { EditGroup.class })
    private Long exampleReportId;

    /**
     * 例报类型
     */
//    @NotBlank(message = "例报类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;

    /**
     * 例报周期
     */
    @NotBlank(message = "例报周期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String cycle;

    /**
     * 例报内容
     */
    @NotBlank(message = "例报内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 推送方式
     */
    @NotBlank(message = "推送方式不能为空", groups = { AddGroup.class, EditGroup.class })
    private String pushMethod;

    /**
     * 接收人id
     */
//    @NotBlank(message = "接收人id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String userId;

    /**
     * 接收人
     */
    @NotBlank(message = "接收人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String receiver;

    /**
     * 开始日期
     */
    @NotNull(message = "开始日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String startDate;

    /**
     * 结束日期
     */
    @NotNull(message = "结束日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String endDate;


}
