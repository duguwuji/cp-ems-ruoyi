package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 功率统计业务对象 power_statistics
 *
 * @author ruoyi
 * @date 2023-09-08
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PowerStatisticsBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 设备编号
     */
    @NotBlank(message = "设备编号不能为空", groups = { AddGroup.class, EditGroup.class })
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
     * 谷值
     */
    @NotNull(message = "谷值不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal min;

    /**
     * 峰值
     */
    @NotNull(message = "峰值不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal max;

    /**
     * 均值
     */
    @NotNull(message = "均值不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal ave;


}
