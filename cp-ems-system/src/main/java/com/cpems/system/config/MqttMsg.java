package com.cpems.system.config;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * MQTT消息基类
 *
 * @Author cpems
 * @Date 2023/4/7 14:04
 */
@Data
@NoArgsConstructor
public class MqttMsg {

    /**
     * 设备id
     */
    private String clientId;

    /**
     * 参数值
     */
    private BigDecimal value;

    /**
     * 创建时间
     */
    private String createTime;

}

