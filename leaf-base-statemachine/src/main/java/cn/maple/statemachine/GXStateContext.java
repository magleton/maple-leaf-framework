package cn.maple.statemachine;

/**
 * GXStateContext
 */
public interface GXStateContext<S, E, C> {
    /**
     * Gets the transition.
     *
     * @return the transition
     */
    GXTransition<S, E, C> getTransition();

    /**
     * Gets the state machine.
     *
     * @return the state machine
     */
    GXStateMachine<S, E, C> getStateMachine();
}
