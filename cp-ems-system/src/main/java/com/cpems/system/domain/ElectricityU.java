package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 电压值对象 ElectricityU
 *
 * @author cpems
 * @date 2023-04-20
 */
@Data
@TableName("electricity_u")
public class ElectricityU {

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
     * 电压值
     */
    private BigDecimal value;

    /**
     * 创建时间
     */
    private Date createTime;

}
