package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;


/**
 * 电能超级表 energy
 *
 * @author cpems
 * @date 2023-04-21
 */

@Data
public class EnergyBo extends BaseEntity {

    /**
     * 时间戳
     */
    @NotNull(message = "时间戳不能为空", groups = {EditGroup.class})
    private Timestamp ts;

    /**
     * 设备id
     */
    @NotBlank(message = "设备id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String clientId;

    /**
     * 能源值
     */
    @NotBlank(message = "电流值不能为空", groups = {AddGroup.class, EditGroup.class})
    private String val;

    /**
     * TAGS：类型
     */
    @NotBlank(message = "电能源类型", groups = {AddGroup.class, EditGroup.class})
    private String type;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 能源类型
     */
    private String energyType;
}
