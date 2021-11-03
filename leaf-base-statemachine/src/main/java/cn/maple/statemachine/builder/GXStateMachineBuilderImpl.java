package cn.maple.statemachine.builder;

import cn.maple.statemachine.GXState;
import cn.maple.statemachine.GXStateMachine;
import cn.maple.statemachine.GXStateMachineFactory;
import cn.maple.statemachine.impl.GXStateMachineImpl;
import cn.maple.statemachine.impl.GXTransitionType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GXStateMachineBuilderImpl
 */
public class GXStateMachineBuilderImpl<S, E, C> implements GXStateMachineBuilder<S, E, C> {
    /**
     * StateMap is the same with stateMachine, as the core of state machine is holding reference to states.
     */
    private final Map<S, GXState<S, E, C>> stateMap = new ConcurrentHashMap<>();
    private final GXStateMachineImpl<S, E, C> stateMachine = new GXStateMachineImpl<>(stateMap);

    @Override
    public GXExternalTransitionBuilder<S, E, C> externalTransition() {
        return new GXTransitionBuilderImpl<>(stateMap, GXTransitionType.EXTERNAL);
    }

    @Override
    public GXExternalTransitionsBuilder<S, E, C> externalTransitions() {
        return new GXTransitionsBuilderImpl<>(stateMap, GXTransitionType.EXTERNAL);
    }

    @Override
    public GXInternalTransitionBuilder<S, E, C> internalTransition() {
        return new GXTransitionBuilderImpl<>(stateMap, GXTransitionType.INTERNAL);
    }

    @Override
    public GXStateMachine<S, E, C> build(String machineId) {
        stateMachine.setMachineId(machineId);
        stateMachine.setReady(true);
        GXStateMachineFactory.register(stateMachine);
        return stateMachine;
    }

}
