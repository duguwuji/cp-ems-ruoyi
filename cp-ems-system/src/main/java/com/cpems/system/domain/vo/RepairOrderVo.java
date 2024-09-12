package com.cpems.system.domain.vo;

import java.util.Date;
import java.util.List;

import com.cpems.common.core.domain.BaseEntity;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 维修工单视图对象 repair_order
 *
 * @author ruoyi
 * @date 2023-04-04
 */
@Data
@ExcelIgnoreUnannotated
public class RepairOrderVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 工单编号
     */
    @ExcelProperty(value = "工单编号")
    private String orderNo;

    /**
     * 工单内容
     */
    @ExcelProperty(value = "工单内容")
    private String orderContent;

    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称")
    private String projectName;

    /**
     * 附件
     */
    @ExcelProperty(value = "附件")
    private String annex;

    /**
     * 派单时间
     */
    @ExcelProperty(value = "派单时间")
    private Date assignTime;

    /**
     * 完成时间
     */
    @ExcelProperty(value = "完成时间")
    private Date finishTime;

    /**
     * 完成人
     */
    @ExcelProperty(value = "完成人")
    private String finishBy;

    /**
     * 工单状态
     */
    @ExcelProperty(value = "工单状态")
    private String orderStatus;

    /**
     * 工单备注
     */
    @ExcelProperty(value = "工单备注")
    private String orderRemark;

    /**
     * 完成人id
     */
    private Long userId;

    /**
     * 附件列表
     */
    private List<SysOssVo> ossList;
}
