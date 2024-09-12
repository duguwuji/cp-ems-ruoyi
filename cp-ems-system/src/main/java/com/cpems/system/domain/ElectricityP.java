package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 电功率值对象 electricity_p
 *
 * @author cpems
 * @date 2023-04-21
 */
@Data
@TableName("electricity_p")
public class ElectricityP {

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
     * 电功率值
     */
    private BigDecimal value;

    /**
     * 创建时间
     */
    private Date createTime;

}
