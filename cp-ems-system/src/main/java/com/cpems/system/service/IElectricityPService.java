package com.cpems.system.service;

import com.cpems.system.domain.vo.ElectricityPVo;
import com.cpems.system.domain.bo.ElectricityPBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 电功率值Service接口
 *
 * @author cpems
 * @date 2023-04-21
 */
public interface IElectricityPService {

    /**
     * 查询电功率值
     */
    ElectricityPVo queryById(Long id);

    /**
     * 查询电功率值列表
     */
    TableDataInfo<ElectricityPVo> queryPageList(ElectricityPBo bo, PageQuery pageQuery);

    /**
     * 查询电功率值列表
     */
    List<ElectricityPVo> queryList(ElectricityPBo bo);

    /**
     * 新增电功率值
     */
    Boolean insertByBo(ElectricityPBo bo);

    /**
     * 修改电功率值
     */
    Boolean updateByBo(ElectricityPBo bo);

    /**
     * 校验并批量删除电功率值信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
