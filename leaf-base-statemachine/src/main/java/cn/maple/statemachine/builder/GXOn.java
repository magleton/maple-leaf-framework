package cn.maple.statemachine.builder;

import cn.maple.statemachine.GXCondition;

/**
 * GXOn
 */
public interface GXOn<S, E, C> extends GXWhen<S, E, C> {
    /**
     * Add condition for the transition
     *
     * @param condition transition condition
     * @return When clause builder
     */
    GXWhen<S, E, C> when(GXCondition<C> condition);
}
