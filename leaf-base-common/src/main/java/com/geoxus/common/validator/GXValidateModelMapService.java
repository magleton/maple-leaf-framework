package com.geoxus.common.validator;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.framework.exception.GXBusinessException;

public interface GXValidateModelMapService {
    boolean validateModelMap(Dict o, String modelName) throws GXBusinessException;
}
