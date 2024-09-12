package com.cpems.system.service;

import com.cpems.system.domain.vo.EquipmentInfoVo;
import com.cpems.system.domain.bo.EquipmentInfoBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 设备信息Service接口
 *
 * @author ruoyi
 * @date 2023-04-06
 */
public interface IEquipmentInfoService {

    /**
     * 查询设备信息
     */
    EquipmentInfoVo queryById(Long id);

    /**
     * 查询设备信息列表
     */
    TableDataInfo<EquipmentInfoVo> queryPageList(EquipmentInfoBo bo, PageQuery pageQuery);

    /**
     * 查询设备信息列表
     */
    List<EquipmentInfoVo> queryList(EquipmentInfoBo bo);

    /**
     * 新增设备信息
     */
    Boolean insertByBo(EquipmentInfoBo bo);

    /**
     * 修改设备信息
     */
    Boolean updateByBo(EquipmentInfoBo bo);

    /**
     * 校验并批量删除设备信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 获取全部不同状态的设备数量及占比
     */
    List<EquipmentInfoVo> getAllStatus();

    /**
     * 判断设备是否已经绑定了区域
     *
     * @param ids
     * @return
     */
    Boolean hasBindArea(Collection<Long> ids);

}
