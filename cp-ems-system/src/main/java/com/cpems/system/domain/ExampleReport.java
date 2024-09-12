package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * 例报管理对象 example_report
 *
 * @author cpems
 * @date 2023-09-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("example_report")
public class ExampleReport extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 例报id
     */
    @TableId(value = "example_report_id")
    private Long exampleReportId;
    /**
     * 例报类型
     */
    private String type;
    /**
     * 例报周期
     */
    private String cycle;
    /**
     * 例报内容
     */
    private String content;
    /**
     * 推送方式
     */
    private String pushMethod;
    /**
     * 接收人id
     */
    private String userId;
    /**
     * 接收人
     */
    private String receiver;
    /**
     * 开始日期
     */
    private Date startDate;
    /**
     * 结束日期
     */
    private Date endDate;

}
