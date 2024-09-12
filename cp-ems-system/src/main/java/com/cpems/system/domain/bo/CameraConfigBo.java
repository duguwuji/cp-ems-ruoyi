package com.cpems.system.domain.bo;

import com.cpems.common.core.domain.BaseEntity;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 视频配置业务对象 camera_config
 *
 * @author ruoyi
 * @date 2023-04-04
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CameraConfigBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 摄像头名称
     */
    @NotBlank(message = "摄像头名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String cameraName;

    /**
     * 品牌
     */
    @NotBlank(message = "品牌不能为空", groups = { AddGroup.class, EditGroup.class })
    private String cameraBrand;

    /**
     * 序列号
     */
    @NotBlank(message = "序列号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String cameraSn;

    /**
     * 视频token
     */
    @NotBlank(message = "视频token不能为空", groups = { AddGroup.class, EditGroup.class })
    private String cameraToken;

    /**
     * 视频key
     */
    @NotBlank(message = "视频key不能为空", groups = { AddGroup.class, EditGroup.class })
    private String cameraKey;

    /**
     * 视频secret
     */
    @NotBlank(message = "视频secret不能为空", groups = { AddGroup.class, EditGroup.class })
    private String cameraSecret;

    /**
     * 区域id
     */
    @NotNull(message = "地点不能为空", groups = { EditGroup.class })
    private Long areaId;

}
