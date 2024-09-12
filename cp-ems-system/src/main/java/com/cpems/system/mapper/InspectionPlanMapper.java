package com.cpems.system.mapper;

import com.cpems.system.domain.InspectionPlan;
import com.cpems.system.domain.vo.InspectionPlanVo;
import com.cpems.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 巡检计划Mapper接口
 *
 * @author ruoyi
 * @date 2023-04-04
 */
@Mapper
public interface InspectionPlanMapper extends BaseMapperPlus<InspectionPlanMapper, InspectionPlan, InspectionPlanVo> {

}
