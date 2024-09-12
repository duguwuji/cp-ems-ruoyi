package com.cpems.system.service;

import com.cpems.system.domain.vo.QuotaConfigVo;
import com.cpems.system.domain.bo.QuotaConfigBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 定额配置Service接口
 *
 * @author cpems
 * @date 2023-09-12
 */
public interface IQuotaConfigService {

    /**
     * 查询定额配置
     */
    QuotaConfigVo queryById(Long quotaId);

    /**
     * 查询定额配置列表
     */
    TableDataInfo<QuotaConfigVo> queryPageList(QuotaConfigBo bo, PageQuery pageQuery);

    /**
     * 查询定额配置列表
     */
    List<QuotaConfigVo> queryList(QuotaConfigBo bo);

    /**
     * 新增定额配置
     */
    Boolean insertByBo(QuotaConfigBo bo);

    /**
     * 修改定额配置
     */
    Boolean updateByBo(QuotaConfigBo bo);

    /**
     * 校验并批量删除定额配置信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 定额分析
     * @return
     */
    Map<String, Object> analysis(Long itemId, String quotaTime);

    /**
     * 用量监测
     * @return
     */
    Map<String, Object> monitor(Long itemId, String quotaTime,String quotaType);
}

