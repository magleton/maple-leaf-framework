package cn.maple.statemachine.builder;

import cn.maple.statemachine.GXAction;

/**
 * GXWhen
 */
public interface GXWhen<S, E, C> {
    /**
     * Define action to be performed during transition
     *
     * @param action performed action
     */
    void perform(GXAction<S, E, C> action);
}
