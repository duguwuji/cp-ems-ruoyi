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
 * 定额配置业务对象 quota_config
 *
 * @author cpems
 * @date 2023-09-12
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class QuotaConfigBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long quotaId;

    /**
     * 定额类型（0 月度，1 年度）
     */
    @NotBlank(message = "定额类型（0 月度，1 年度）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String quotaType;

    /**
     * 区域
     */
    @NotNull(message = "区域不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long itemId;

    /**
     * 定额时间
     */
    @NotNull(message = "定额时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date quotaTime;

    /**
     * 定额数值
     */
    @NotNull(message = "定额数值不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal quotaValue;

    /**
     * 实际累计能耗
     */
//    @NotNull(message = "实际累计能耗不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal realEnergy;

    /**
     * 临界范围
     */
    @NotNull(message = "临界范围不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal critical;

    /**
     * 越限范围
     */
    @NotNull(message = "越限范围不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal overMedian;


}
