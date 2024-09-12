package com.cpems.system.service;

import com.cpems.system.domain.ChargingStep;
import com.cpems.system.domain.vo.ChargingStepVo;
import com.cpems.system.domain.bo.ChargingStepBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 阶梯计费参数信息Service接口
 *
 * @author ruoyi
 * @date 2023-10-31
 */
public interface IChargingStepService {

    /**
     * 查询阶梯计费参数信息
     */
    ChargingStepVo queryById(Long id);

    /**
     * 查询阶梯计费参数信息列表
     */
    TableDataInfo<ChargingStepVo> queryPageList(ChargingStepBo bo, PageQuery pageQuery);

    /**
     * 查询阶梯计费参数信息列表
     */
    List<ChargingStepVo> queryList(ChargingStepBo bo);

    /**
     * 新增阶梯计费参数信息
     */
    Boolean insertByBo(ChargingStepBo bo);

    /**
     * 修改阶梯计费参数信息
     */
    Boolean updateByBo(ChargingStepBo bo);

    /**
     * 校验并批量删除阶梯计费参数信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 更新充电策略的所有价格参数
     */
    Boolean updateChargingStepList(Collection<ChargingStep> paramList);
}
