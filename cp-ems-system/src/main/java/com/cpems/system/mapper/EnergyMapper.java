package com.cpems.system.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.cpems.common.core.mapper.BaseMapperPlus;
import com.cpems.system.domain.Energy;
import com.cpems.system.domain.vo.EnergyVo;


/**
 * 能源值Mapper接口
 *
 * @author cpems
 * @date 2023-04-21
 */
@DS("td")
public interface EnergyMapper extends BaseMapperPlus<EnergyMapper, Energy, EnergyVo> {

}
