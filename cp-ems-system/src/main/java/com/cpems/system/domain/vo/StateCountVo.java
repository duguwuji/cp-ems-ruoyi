package com.cpems.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 工单、巡检数量统计
 * @Author cpems
 * @Date 2023/11/2 13:16
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StateCountVo {
    /**
     * 时间
     */
    private String time;
    /**
     * 已完成数量
     */
    private Long finished;
    /**
     * 未完成数量
     */
    private Long unfinished;
}
