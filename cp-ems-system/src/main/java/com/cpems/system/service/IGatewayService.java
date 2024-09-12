package com.cpems.system.service;

import com.cpems.system.domain.vo.GatewayVo;
import com.cpems.system.domain.bo.GatewayBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 网关信息Service接口
 *
 * @author ruoyi
 * @date 2023-04-10
 */
public interface IGatewayService {

    /**
     * 查询网关信息
     */
    GatewayVo queryById(Long id);

    /**
     * 查询网关信息列表
     */
    TableDataInfo<GatewayVo> queryPageList(GatewayBo bo, PageQuery pageQuery);

    /**
     * 查询网关信息列表
     */
    List<GatewayVo> queryList(GatewayBo bo);

    /**
     * 新增网关信息
     */
    Boolean insertByBo(GatewayBo bo);

    /**
     * 修改网关信息
     */
    Boolean updateByBo(GatewayBo bo);

    /**
     * 校验并批量删除网关信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
