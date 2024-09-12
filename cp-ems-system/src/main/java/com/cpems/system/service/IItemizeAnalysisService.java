package com.cpems.system.service;

import com.cpems.system.domain.vo.ItemizeVo;

import java.util.List;

/**
 * 分项分析Service接口
 *
 * @Author cpems
 * @Date 2023/9/5 11:33
 */
public interface IItemizeAnalysisService {

    /**
     * 分项概况
     */
    List<ItemizeVo> getOverview(String itemId, String dateType, String date);
}
