package com.cpems.system.service;

import com.cpems.system.domain.bo.BrandBo;
import com.cpems.system.domain.vo.BrandVo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 品牌信息Service接口
 *
 * @author cpems
 * @date 2023-10-10
 */
public interface IBrandService {

    /**
     * 查询品牌信息
     */
    BrandVo queryById(Long id);

    /**
     * 查询品牌信息列表
     */
    TableDataInfo<BrandVo> queryPageList(BrandBo bo, PageQuery pageQuery);

    /**
     * 查询品牌信息列表
     */
    List<BrandVo> queryList(BrandBo bo);

    /**
     * 新增品牌信息
     */
    Boolean insertByBo(BrandBo bo);

    /**
     * 修改品牌信息
     */
    Boolean updateByBo(BrandBo bo);

    /**
     * 校验并批量删除品牌信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
