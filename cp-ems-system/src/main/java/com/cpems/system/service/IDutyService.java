package com.cpems.system.service;

import com.cpems.system.domain.vo.DutyVo;
import com.cpems.system.domain.bo.DutyBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 值班管理Service接口
 *
 * @author ruoyi
 * @date 2023-09-11
 */
public interface IDutyService {

    /**
     * 查询值班管理
     */
    DutyVo queryById(Long dutyId);

    /**
     * 查询值班管理列表
     */
    TableDataInfo<DutyVo> queryPageList(DutyBo bo, PageQuery pageQuery);

    /**
     * 查询值班管理列表
     */
    List<DutyVo> queryList(DutyBo bo);

    /**
     * 新增值班管理
     */
    Boolean insertByBo(DutyBo bo);

    /**
     * 修改值班管理
     */
    Boolean updateByBo(DutyBo bo);

    /**
     * 校验并批量删除值班管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
