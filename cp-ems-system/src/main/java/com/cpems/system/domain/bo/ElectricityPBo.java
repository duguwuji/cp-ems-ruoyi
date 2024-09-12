package com.cpems.system.domain.bo;

import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import javax.validation.constraints.*;
import java.util.Date;


/**
 * 电功率值业务对象 electricity_p
 *
 * @author cpems
 * @date 2023-04-21
 */

@Data
public class ElectricityPBo {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 设备id
     */
    @NotBlank(message = "设备id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String clientId;

    /**
     * 电功率值
     */
    @NotBlank(message = "电功率值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String value;

    /**
     * 创建时间
     */
    private Date createTime;

}
