package com.cpems.system.service;

import com.cpems.system.domain.vo.ExampleReportVo;
import com.cpems.system.domain.bo.ExampleReportBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 例报管理Service接口
 *
 * @author ruoyi
 * @date 2023-09-11
 */
public interface IExampleReportService {

    /**
     * 查询例报管理
     */
    ExampleReportVo queryById(Long exampleReportId);

    /**
     * 查询例报管理列表
     */
    TableDataInfo<ExampleReportVo> queryPageList(ExampleReportBo bo, PageQuery pageQuery);

    /**
     * 查询例报管理列表
     */
    List<ExampleReportVo> queryList(ExampleReportBo bo);

    /**
     * 新增例报管理
     */
    Boolean insertByBo(ExampleReportBo bo);

    /**
     * 修改例报管理
     */
    Boolean updateByBo(ExampleReportBo bo);

    /**
     * 校验并批量删除例报管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
