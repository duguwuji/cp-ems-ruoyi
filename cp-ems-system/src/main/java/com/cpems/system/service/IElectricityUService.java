package com.cpems.system.service;

import com.cpems.system.domain.vo.ElectricityUVo;
import com.cpems.system.domain.bo.ElectricityUBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 电压值Service接口
 *
 * @author cpems
 * @date 2023-04-20
 */
public interface IElectricityUService {

    /**
     * 查询电压值
     */
    ElectricityUVo queryById(Long id);

    /**
     * 查询电压值列表
     */
    TableDataInfo<ElectricityUVo> queryPageList(ElectricityUBo bo, PageQuery pageQuery);

    /**
     * 查询电压值列表
     */
    List<ElectricityUVo> queryList(ElectricityUBo bo);

    /**
     * 新增电压值
     */
    Boolean insertByBo(ElectricityUBo bo);

    /**
     * 修改电压值
     */
    Boolean updateByBo(ElectricityUBo bo);

    /**
     * 校验并批量删除电压值信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
