package com.cpems.system.service;

import com.cpems.system.domain.vo.AlarmRuleVo;
import com.cpems.system.domain.bo.AlarmRuleBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 报警规则Service接口
 *
 * @author ruoyi
 * @date 2023-04-06
 */
public interface IAlarmRuleService {

    /**
     * 查询报警规则
     */
    AlarmRuleVo queryById(Long id);

    /**
     * 查询报警规则列表
     */
    TableDataInfo<AlarmRuleVo> queryPageList(AlarmRuleBo bo, PageQuery pageQuery);

    /**
     * 查询报警规则列表
     */
    List<AlarmRuleVo> queryList(AlarmRuleBo bo);

    /**
     * 新增报警规则
     */
    Boolean insertByBo(AlarmRuleBo bo);

    /**
     * 修改报警规则
     */
    Boolean updateByBo(AlarmRuleBo bo);

    /**
     * 校验并批量删除报警规则信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
