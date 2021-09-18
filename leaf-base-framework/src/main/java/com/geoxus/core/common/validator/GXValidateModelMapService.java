package com.geoxus.core.common.validator;

import cn.hutool.core.lang.Dict;
import com.geoxus.common.exception.GXBusinessException;

public interface GXValidateModelMapService {
    boolean validateModelMap(Dict o, String modelName) throws GXBusinessException;
}
