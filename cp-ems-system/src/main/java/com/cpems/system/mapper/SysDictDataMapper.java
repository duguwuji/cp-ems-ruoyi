package com.cpems.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cpems.common.constant.UserConstants;
import com.cpems.common.core.domain.entity.SysDictData;
import com.cpems.common.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author cpems
 */
public interface SysDictDataMapper extends BaseMapperPlus<SysDictDataMapper, SysDictData, SysDictData> {

    default List<SysDictData> selectDictDataByType(String dictType) {
        return selectList(
            new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getStatus, UserConstants.DICT_NORMAL)
                .eq(SysDictData::getDictType, dictType)
                .orderByAsc(SysDictData::getDictSort));
    }
}
