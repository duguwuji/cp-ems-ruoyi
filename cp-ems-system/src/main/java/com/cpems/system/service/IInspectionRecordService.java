package com.cpems.system.service;

import com.cpems.system.domain.vo.InspectionRecordVo;
import com.cpems.system.domain.bo.InspectionRecordBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 巡检记录Service接口
 *
 * @author ruoyi
 * @date 2023-04-04
 */
public interface IInspectionRecordService {

    /**
     * 查询巡检记录
     */
    InspectionRecordVo queryById(Long id);

    /**
     * 查询巡检记录列表
     */
    TableDataInfo<InspectionRecordVo> queryPageList(InspectionRecordBo bo, PageQuery pageQuery);

    /**
     * 查询巡检记录列表
     */
    List<InspectionRecordVo> queryList(InspectionRecordBo bo);

    /**
     * 新增巡检记录
     */
    Boolean insertByBo(InspectionRecordBo bo);

    /**
     * 修改巡检记录
     */
    Boolean updateByBo(InspectionRecordBo bo);

    /**
     * 校验并批量删除巡检记录信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
