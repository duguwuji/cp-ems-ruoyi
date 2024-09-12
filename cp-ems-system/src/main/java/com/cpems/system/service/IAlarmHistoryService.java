package com.cpems.system.service;

import com.cpems.system.domain.vo.AlarmHistoryVo;
import com.cpems.system.domain.bo.AlarmHistoryBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 实时报警Service接口
 *
 * @author ruoyi
 * @date 2023-04-04
 */
public interface IAlarmHistoryService {

    /**
     * 查询实时报警
     */
    AlarmHistoryVo queryById(Long id);

    /**
     * 查询实时报警列表
     */
    TableDataInfo<AlarmHistoryVo> queryPageList(AlarmHistoryBo bo, PageQuery pageQuery);

    /**
     * 查询实时报警列表
     */
    List<AlarmHistoryVo> queryList(AlarmHistoryBo bo);

    /**
     * 新增实时报警
     */
    Boolean insertByBo(AlarmHistoryBo bo);

    /**
     * 修改实时报警
     */
    Boolean updateByBo(AlarmHistoryBo bo);

    /**
     * 校验并批量删除实时报警信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
