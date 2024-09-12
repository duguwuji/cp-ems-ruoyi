package com.cpems.demo.mapper;

import com.cpems.common.annotation.DataColumn;
import com.cpems.common.annotation.DataPermission;
import com.cpems.common.core.mapper.BaseMapperPlus;
import com.cpems.demo.domain.TestTree;
import com.cpems.demo.domain.vo.TestTreeVo;

/**
 * 测试树表Mapper接口
 *
 * @author cpems
 * @date 2021-07-26
 */
@DataPermission({
    @DataColumn(key = "deptName", value = "dept_id"),
    @DataColumn(key = "userName", value = "user_id")
})
public interface TestTreeMapper extends BaseMapperPlus<TestTreeMapper, TestTree, TestTreeVo> {

}
