package com.geoxus.common.mapstruct;

import com.geoxus.common.dto.GXBaseData;
import com.geoxus.common.dto.GXBaseDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.MappingTarget;

/**
 * 标记接口
 *
 * @author britton <britton@126.com>
 * @version 1.0
 * @since 2020-09-26
 */
@SuppressWarnings("all")
public interface GXBaseMapStruct<S extends GXBaseDto, T extends GXBaseData> {
    @BeforeMapping
    default void beforeMapping(S source) {
        source.verify();
        source.repair();
    }

    @AfterMapping
    default void afterMapping(@MappingTarget T target) {
        target.verify();
        target.repair();
    }
}
