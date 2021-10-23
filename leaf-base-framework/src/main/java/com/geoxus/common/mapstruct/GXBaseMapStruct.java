package com.geoxus.common.mapstruct;

import com.geoxus.common.dto.GXBaseData;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * 基础转换接口
 * 也可自行定义方法
 * 调用规则: 方法签名一致即可被Mapstruct自动调用 (Ljava/util/List;)Ljava/util/List;
 * call rule : The generated code will invoke the default methods if the argument and return types match
 *
 * @author britton <britton@126.com>
 * @version 1.0
 * @since 2020-09-26
 */
@SuppressWarnings("all")
public interface GXBaseMapStruct<S extends GXBaseData, T extends GXBaseData> {
    /**
     * 转换之前的回调
     *
     * @param source
     */
    @BeforeMapping
    default void beforeMapping(S source) {
        source.customizeProcess();
    }

    /**
     * 转换完成之后的回调
     *
     * @param source
     * @param target
     */
    @AfterMapping
    default void afterMapping(S source, @MappingTarget T target) {
        target.customizeProcess();
    }

    /**
     * 将源对象转换为目标对象
     *
     * @param source
     * @return
     * @author britton
     * @since 2021-10-23
     */
    T sourceToTarget(S source);

    /**
     * 将源对象列表转换为目标对象列表
     *
     * @param sourceList
     * @return
     * @author britton
     * @since 2021-10-23
     */
    List<T> sourceListToTargetList(List<S> sourceList);

    /**
     * 将目标对象转换为源对象
     *
     * @param target
     * @return
     * @author britton
     * @since 2021-10-23
     */
    S targetToSource(T target);

    /**
     * 将目标对象列表转换为源对象列表
     *
     * @param targetList
     * @return
     * @author britton
     * @since 2021-10-23
     */
    List<S> targetListToSourceList(List<T> targetList);
}
