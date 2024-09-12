package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 电流值对象 electricity_i
 *
 * @author cpems
 * @date 2023-04-21
 */
@Data
@TableName("electricity_i")
public class ElectricityI {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 设备id
     */
    private String clientId;
    /**
     * 电流值
     */
    private BigDecimal value;

    /**
     * 创建时间
     */
    private Date createTime;

}
