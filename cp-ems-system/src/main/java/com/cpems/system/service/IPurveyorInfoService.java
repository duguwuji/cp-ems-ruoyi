package com.cpems.system.service;

import com.cpems.system.domain.vo.PurveyorInfoVo;
import com.cpems.system.domain.bo.PurveyorInfoBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 供应商库管理Service接口
 *
 * @author cpems
 * @date 2023-09-11
 */
public interface IPurveyorInfoService {

    /**
     * 查询供应商库管理
     */
    PurveyorInfoVo queryById(Long purveyorId);

    /**
     * 查询供应商库管理列表
     */
    TableDataInfo<PurveyorInfoVo> queryPageList(PurveyorInfoBo bo, PageQuery pageQuery);

    /**
     * 查询供应商库管理列表
     */
    List<PurveyorInfoVo> queryList(PurveyorInfoBo bo);

    /**
     * 新增供应商库管理
     */
    Boolean insertByBo(PurveyorInfoBo bo);

    /**
     * 修改供应商库管理
     */
    Boolean updateByBo(PurveyorInfoBo bo);

    /**
     * 校验并批量删除供应商库管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
