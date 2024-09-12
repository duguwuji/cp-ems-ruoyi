package com.cpems.system.service;

import com.cpems.system.domain.vo.ElectricityIVo;
import com.cpems.system.domain.bo.ElectricityIBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 电流值Service接口
 *
 * @author cpems
 * @date 2023-04-21
 */
public interface IElectricityIService {

    /**
     * 查询电流值
     */
    ElectricityIVo queryById(Long id);

    /**
     * 查询电流值列表
     */
    TableDataInfo<ElectricityIVo> queryPageList(ElectricityIBo bo, PageQuery pageQuery);

    /**
     * 查询电流值列表
     */
    List<ElectricityIVo> queryList(ElectricityIBo bo);

    /**
     * 新增电流值
     */
    Boolean insertByBo(ElectricityIBo bo);

    /**
     * 修改电流值
     */
    Boolean updateByBo(ElectricityIBo bo);

    /**
     * 校验并批量删除电流值信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
