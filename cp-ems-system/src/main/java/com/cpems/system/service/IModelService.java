package com.cpems.system.service;

import com.cpems.system.domain.bo.ModelBo;
import com.cpems.system.domain.vo.ModelVo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 型号信息Service接口
 *
 * @author cpems
 * @date 2023-10-11
 */
public interface IModelService {

    /**
     * 查询型号信息
     */
    ModelVo queryById(Long id);

    /**
     * 查询型号信息列表
     */
    TableDataInfo<ModelVo> queryPageList(ModelBo bo, PageQuery pageQuery);

    /**
     * 查询型号信息列表
     */
    List<ModelVo> queryList(ModelBo bo);

    /**
     * 新增型号信息
     */
    Boolean insertByBo(ModelBo bo);

    /**
     * 修改型号信息
     */
    Boolean updateByBo(ModelBo bo);

    /**
     * 校验并批量删除型号信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
