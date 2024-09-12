package com.cpems.system.domain.vo;

import lombok.Data;

/**
 * 用量监测视图
 *
 * @Author cpems
 * @Date 2023/9/14 11:10
 */
@Data
public class DosageVo {
    private static final long serialVersionUID=1L;

    /**
     * 时间
     */
    private String date;

    /**
     * 能耗数据
     */
    private Object energyData;

    /**
     * 定额数据
     */
    private Object quotaData;
}
