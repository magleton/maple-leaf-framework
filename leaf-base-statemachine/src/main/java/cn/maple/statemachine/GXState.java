package cn.maple.statemachine;

import cn.maple.statemachine.impl.GXTransitionType;

import java.util.Collection;
import java.util.List;

/**
 * GXState
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public interface GXState<S, E, C> extends GXVisitable {
    /**
     * Gets the state identifier.
     *
     * @return the state identifiers
     */
    S getId();

    /**
     * Add transition to the state
     *
     * @param event  the event of the Transition
     * @param target the target of the transition
     * @return 转换器
     */
    GXTransition<S, E, C> addTransition(E event, GXState<S, E, C> target, GXTransitionType transitionType);

    /**
     * 获取事件转换器
     *
     * @param event 事件类型
     * @return 列表
     */
    List<GXTransition<S, E, C>> getEventTransitions(E event);

    /**
     * 获取所有转换器
     *
     * @return 列表
     */
    Collection<GXTransition<S, E, C>> getAllTransitions();
}
