package cn.maple.core.framework.mapstruct;

import cn.maple.core.framework.dto.GXBaseData;
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
public interface GXBaseMapStruct<S extends GXBaseData, T extends GXBaseData> {
    /**
     * 转换之前的回调
     *
     * @param source 原对象
     */
    @BeforeMapping
    default void beforeMapping(S source) {
    }

    /**
     * 转换完成之后的回调
     *
     * @param source 原对象
     * @param target 目标对象
     */
    @AfterMapping
    default void afterMapping(S source, @MappingTarget T target) {
    }

    /**
     * 将源对象转换为目标对象
     *
     * @param source 原对象
     * @return 目标对象
     * @author britton
     * @since 2021-10-23
     */
    T sourceToTarget(S source);

    /**
     * 将源对象列表转换为目标对象列表
     *
     * @param sourceList 原对象列表
     * @return 目标List
     * @author britton
     * @since 2021-10-23
     */
    List<T> sourceToTarget(List<S> sourceList);

    /**
     * 将目标对象转换为源对象
     *
     * @param target 目标对象
     * @return 原对象
     * @author britton
     * @since 2021-10-23
     */
    S targetToSource(T target);

    /**
     * 将目标对象列表转换为源对象列表
     *
     * @param targetList 目标对象列表
     * @return 原对象列表
     * @author britton
     * @since 2021-10-23
     */
    List<S> targetToSource(List<T> targetList);
}
