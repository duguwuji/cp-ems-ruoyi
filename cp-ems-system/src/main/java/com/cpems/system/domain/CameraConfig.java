package com.cpems.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cpems.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 视频配置对象 camera_config
 *
 * @author cpems
 * @date 2023-04-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("camera_config")
public class CameraConfig extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 摄像头名称
     */
    private String cameraName;
    /**
     * 品牌
     */
    private String cameraBrand;
    /**
     * 序列号
     */
    private String cameraSn;
    /**
     * 视频token
     */
    private String cameraToken;
    /**
     * 视频key
     */
    private String cameraKey;
    /**
     * 视频secret
     */
    private String cameraSecret;
    /**
     * 区域id
     */
    private Long areaId;

}
