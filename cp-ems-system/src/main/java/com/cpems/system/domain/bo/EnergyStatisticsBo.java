package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;

import javax.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 能耗统计业务对象 energy_statistics
 *
 * @author cpems
 * @date 2023-08-24
 */

@Data
public class EnergyStatisticsBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 设备编号
     */
    @NotNull(message = "设备编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String equipmentSn;

    /**
     * 能源类型（0：电，1：水）
     */
    @NotBlank(message = "能源类型（0：电，1：水）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String energyType;

    /**
     * 时间
     */
    @NotNull(message = "时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date time;

    /**
     * 能耗
     */
    @NotNull(message = "能耗不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal statistics;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

}

