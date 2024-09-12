package com.cpems.system.service;

import com.cpems.system.domain.vo.InspectionPlanVo;
import com.cpems.system.domain.bo.InspectionPlanBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 巡检计划Service接口
 *
 * @author ruoyi
 * @date 2023-04-04
 */
public interface IInspectionPlanService {

    /**
     * 查询巡检计划
     */
    InspectionPlanVo queryById(Long id);

    /**
     * 查询巡检计划列表
     */
    TableDataInfo<InspectionPlanVo> queryPageList(InspectionPlanBo bo, PageQuery pageQuery);

    /**
     * 查询巡检计划列表
     */
    List<InspectionPlanVo> queryList(InspectionPlanBo bo);

    /**
     * 新增巡检计划
     */
    Boolean insertByBo(InspectionPlanBo bo);

    /**
     * 修改巡检计划
     */
    Boolean updateByBo(InspectionPlanBo bo);

    /**
     * 校验并批量删除巡检计划信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 自动巡检产生巡检记录（日）
     */
    Boolean inspectionDayHandler();

    /**
     * 自动巡检产生巡检记录（周）
     */
    Boolean inspectionWeekHandler();

    /**
     * 自动巡检产生巡检记录（月）
     */
    Boolean inspectionMonthHandler();
}
