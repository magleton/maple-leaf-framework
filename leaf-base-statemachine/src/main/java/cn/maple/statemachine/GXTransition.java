package cn.maple.statemachine;

import cn.maple.statemachine.impl.GXTransitionType;

/**
 * {@code GXTransition} is something what a state machine associates with a state
 * changes.
 *
 * @param <S> the type of state
 * @param <E> the type of event
 * @param <C> the type of user defined context, which is used to hold application data
 */
public interface GXTransition<S, E, C> {
    /**
     * Gets the source state of this transition.
     *
     * @return the source state
     */
    GXState<S, E, C> getSource();

    void setSource(GXState<S, E, C> state);

    E getEvent();

    void setEvent(E event);

    void setType(GXTransitionType type);

    /**
     * Gets the target state of this transition.
     *
     * @return the target state
     */
    GXState<S, E, C> getTarget();

    void setTarget(GXState<S, E, C> state);

    /**
     * Gets the guard of this transition.
     *
     * @return the guard
     */
    GXCondition<C> getCondition();

    void setCondition(GXCondition<C> condition);

    GXAction<S, E, C> getAction();

    void setAction(GXAction<S, E, C> action);

    /**
     * Do transition from source state to target state.
     *
     * @return the target state
     */

    GXState<S, E, C> transit(C ctx, boolean checkCondition);

    /**
     * Verify transition correctness
     */
    void verify();
}
