package com.geoxus.core.common.validator;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.exception.GXException;

public interface GXValidateModelMap {
    boolean validateModelMap(Dict o, String modelName) throws GXException;
}
