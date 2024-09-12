package com.cpems.system.service;

import com.cpems.system.domain.vo.CameraConfigVo;
import com.cpems.system.domain.bo.CameraConfigBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 视频配置Service接口
 *
 * @author ruoyi
 * @date 2023-04-04
 */
public interface ICameraConfigService {

    /**
     * 查询视频配置
     */
    CameraConfigVo queryById(Long id);

    /**
     * 查询视频配置列表
     */
    TableDataInfo<CameraConfigVo> queryPageList(CameraConfigBo bo, PageQuery pageQuery);

    /**
     * 查询视频配置列表
     */
    List<CameraConfigVo> queryList(CameraConfigBo bo);

    /**
     * 新增视频配置
     */
    Boolean insertByBo(CameraConfigBo bo);

    /**
     * 修改视频配置
     */
    Boolean updateByBo(CameraConfigBo bo);

    /**
     * 校验并批量删除视频配置信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 通过序列号返回摄像头播放地址
     */
    CameraConfigVo getUrlBySerialNumber(String serialNumber);
}
