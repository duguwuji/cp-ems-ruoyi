package com.cpems.system.service;

import com.cpems.system.domain.vo.ProcessInfoVo;
import com.cpems.system.domain.bo.ProcessInfoBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 流程管理Service接口
 *
 * @author cpems
 * @date 2023-09-11
 */
public interface IProcessInfoService {

    /**
     * 查询流程管理
     */
    ProcessInfoVo queryById(Long processId);

    /**
     * 查询流程管理列表
     */
    TableDataInfo<ProcessInfoVo> queryPageList(ProcessInfoBo bo, PageQuery pageQuery);

    /**
     * 查询流程管理列表
     */
    List<ProcessInfoVo> queryList(ProcessInfoBo bo);

    /**
     * 新增流程管理
     */
    Boolean insertByBo(ProcessInfoBo bo);

    /**
     * 修改流程管理
     */
    Boolean updateByBo(ProcessInfoBo bo);

    /**
     * 校验并批量删除流程管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
