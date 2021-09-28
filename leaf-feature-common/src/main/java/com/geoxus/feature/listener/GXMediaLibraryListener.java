package com.geoxus.feature.listener;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONObject;
import com.geoxus.feature.constant.GXMediaLibraryConstant;
import com.geoxus.common.constant.GXCommonConstant;
import com.geoxus.feature.events.GXMediaLibraryEvent;
import com.geoxus.feature.services.GXMediaLibraryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class GXMediaLibraryListener<T> implements ApplicationListener<GXMediaLibraryEvent<T>> {
    @Autowired
    private GXMediaLibraryService coreMediaLibraryService;

    @Override
    public void onApplicationEvent(@NonNull GXMediaLibraryEvent<T> event) {
        dealData(event);
    }

    public void dealData(GXMediaLibraryEvent<T> event) {
        final Dict param = event.getParam();
        final long coreModelId = param.getInt(GXCommonConstant.CORE_MODEL_PRIMARY_FIELD_NAME);
        final long targetId = param.getLong(GXMediaLibraryConstant.TARGET_ID_FIELD_NAME);
        if (targetId > 0) {
            Dict condition = Dict.create();
            if (null != param.getObj("condition")) {
                condition = Convert.convert(Dict.class, param.getObj("condition"));
            }
            coreMediaLibraryService.updateOwner(targetId, coreModelId, Convert.convert(new TypeReference<List<JSONObject>>() {
            }, param.getObj("media")), condition);
        }
    }
}
