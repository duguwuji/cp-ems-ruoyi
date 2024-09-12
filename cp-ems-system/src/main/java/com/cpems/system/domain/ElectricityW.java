package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 总用电量值对象 electricity_w
 *
 * @author cpems
 * @date 2023-04-21
 */
@Data
@TableName("electricity_w")
public class ElectricityW {

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
     * 总用电量值
     */
    private BigDecimal value;
    /**
     * 创建时间
     */
    private Date createTime;

}
