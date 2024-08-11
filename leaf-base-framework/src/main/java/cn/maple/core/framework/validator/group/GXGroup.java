package cn.maple.core.framework.validator.group;

import jakarta.validation.GroupSequence;

/**
 * 定义校验顺序，如果AddGroup组失败，则UpdateGroup组不会再校验
 *
 * @author britton britton@126.com
 */
@GroupSequence({GXAddGroup.class, GXUpdateGroup.class})
public interface GXGroup {
}
