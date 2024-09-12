package com.cpems.system.service;

import com.cpems.system.domain.vo.RegulationInfoVo;
import com.cpems.system.domain.bo.RegulationInfoBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 制度管理Service接口
 *
 * @author cpems
 * @date 2023-09-11
 */
public interface IRegulationInfoService {

    /**
     * 查询制度管理
     */
    RegulationInfoVo queryById(Long regulationId);

    /**
     * 查询制度管理列表
     */
    TableDataInfo<RegulationInfoVo> queryPageList(RegulationInfoBo bo, PageQuery pageQuery);

    /**
     * 查询制度管理列表
     */
    List<RegulationInfoVo> queryList(RegulationInfoBo bo);

    /**
     * 新增制度管理
     */
    Boolean insertByBo(RegulationInfoBo bo);

    /**
     * 修改制度管理
     */
    Boolean updateByBo(RegulationInfoBo bo);

    /**
     * 校验并批量删除制度管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
