package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 维修工单对象 repair_order
 *
 * @author cpems
 * @date 2023-04-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("repair_order")
public class RepairOrder extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 工单编号
     */
    private String orderNo;
    /**
     * 工单内容
     */
    private String orderContent;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 附件
     */
    private String annex;
    /**
     * 派单时间
     */
    private Date assignTime;
    /**
     * 完成时间
     */
    private Date finishTime;
    /**
     * 完成人
     */
    private String finishBy;
    /**
     * 工单状态
     */
    private String orderStatus;
    /**
     * 工单备注
     */
    private String orderRemark;
    /**
     * 完成人id
     */
    private Long userId;

}
