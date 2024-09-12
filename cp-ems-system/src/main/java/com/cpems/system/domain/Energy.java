package com.cpems.system.domain;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;


/**
 * 能源超级表  energy
 *
 * @author cpems
 * @date 2023-06-19
 */
@Data
@TableName("energy")
@ExcelIgnoreUnannotated
public class Energy {

    private static final long serialVersionUID = 1L;

    /**
     * 时间戳
     */
    private Timestamp ts;

    /**
     * 设备id
     */
    @TableField("client_id")
    private String clientId;

    /**
     * 能源值
     */
    private float val;

    /**
     * TAGS：类型
     */
    private String type;

}
