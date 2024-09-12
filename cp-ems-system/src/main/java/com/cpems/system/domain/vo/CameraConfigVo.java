package com.cpems.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 视频配置视图对象 camera_config
 *
 * @author ruoyi
 * @date 2023-04-04
 */
@Data
@ExcelIgnoreUnannotated
public class CameraConfigVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 摄像头名称
     */
    @ExcelProperty(value = "摄像头名称")
    private String cameraName;

    /**
     * 品牌
     */
    @ExcelProperty(value = "品牌")
    private String cameraBrand;

    /**
     * 序列号
     */
    @ExcelProperty(value = "序列号")
    private String cameraSn;

    /**
     * 视频token
     */
    @ExcelProperty(value = "视频token")
    private String cameraToken;

    /**
     * 视频key
     */
    @ExcelProperty(value = "视频key")
    private String cameraKey;

    /**
     * 视频secret
     */
    @ExcelProperty(value = "视频secret")
    private String cameraSecret;

    /**
     * 播放地址
     */
    private String url;

    /**
     * 区域id
     */
    private Long areaId;

    /**
     * 区域
     */
    private ItemTopologyVo area;

}
