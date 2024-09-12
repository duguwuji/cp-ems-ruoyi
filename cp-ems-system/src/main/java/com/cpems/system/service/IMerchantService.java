package com.cpems.system.service;

import com.cpems.system.domain.vo.MerchantVo;
import com.cpems.system.domain.bo.MerchantBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 商户信息Service接口
 *
 * @author ruoyi
 * @date 2023-10-07
 */
public interface IMerchantService {

    /**
     * 查询商户信息
     */
    MerchantVo queryById(Long merchantId);

    /**
     * 查询商户信息列表
     */
    TableDataInfo<MerchantVo> queryPageList(MerchantBo bo, PageQuery pageQuery);

    /**
     * 查询商户信息列表
     */
    List<MerchantVo> queryList(MerchantBo bo);

    /**
     * 新增商户信息
     */
    Boolean insertByBo(MerchantBo bo);

    /**
     * 修改商户信息
     */
    Boolean updateByBo(MerchantBo bo);

    /**
     * 校验并批量删除商户信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
