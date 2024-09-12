package com.cpems.system.service;

import com.cpems.system.domain.vo.StandardInfoVo;
import com.cpems.system.domain.bo.StandardInfoBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 作业规范Service接口
 *
 * @author cpems
 * @date 2023-09-11
 */
public interface IStandardInfoService {

    /**
     * 查询作业规范
     */
    StandardInfoVo queryById(Long standardId);

    /**
     * 查询作业规范列表
     */
    TableDataInfo<StandardInfoVo> queryPageList(StandardInfoBo bo, PageQuery pageQuery);

    /**
     * 查询作业规范列表
     */
    List<StandardInfoVo> queryList(StandardInfoBo bo);

    /**
     * 新增作业规范
     */
    Boolean insertByBo(StandardInfoBo bo);

    /**
     * 修改作业规范
     */
    Boolean updateByBo(StandardInfoBo bo);

    /**
     * 校验并批量删除作业规范信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
