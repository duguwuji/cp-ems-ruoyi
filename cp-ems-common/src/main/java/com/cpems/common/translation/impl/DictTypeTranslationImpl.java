package com.cpems.common.translation.impl;

import com.cpems.common.annotation.TranslationType;
import com.cpems.common.constant.TransConstant;
import com.cpems.common.core.service.DictService;
import com.cpems.common.translation.TranslationInterface;
import com.cpems.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 字典翻译实现
 *
 * @author cpems
 */
@Component
@AllArgsConstructor
@TranslationType(type = TransConstant.DICT_TYPE_TO_LABEL)
public class DictTypeTranslationImpl implements TranslationInterface<String> {

    private final DictService dictService;

    public String translation(Object key, String other) {
        if (key instanceof String && StringUtils.isNotBlank(other)) {
            return dictService.getDictLabel(other, key.toString());
        }
        return null;
    }
}
