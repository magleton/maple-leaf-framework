package cn.maple.statemachine.builder;

import cn.maple.statemachine.GXStateMachine;

/**
 * GXStateMachineBuilder
 */
public interface GXStateMachineBuilder<S, E, C> {
    /**
     * Builder for one transition
     *
     * @return External transition builder
     */
    GXExternalTransitionBuilder<S, E, C> externalTransition();

    /**
     * Builder for multiple transitions
     *
     * @return External transition builder
     */
    GXExternalTransitionsBuilder<S, E, C> externalTransitions();

    /**
     * Start to build internal transition
     *
     * @return Internal transition builder
     */
    GXInternalTransitionBuilder<S, E, C> internalTransition();

    GXStateMachine<S, E, C> build(String machineId);

}
