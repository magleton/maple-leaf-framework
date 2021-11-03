package cn.maple.statemachine.impl;

import cn.maple.statemachine.GXState;
import cn.maple.statemachine.GXStateMachine;
import cn.maple.statemachine.GXTransition;
import cn.maple.statemachine.GXVisitor;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * For performance consideration,
 * The state machine is made "stateless" on purpose.
 * Once it's built, it can be shared by multi-thread
 * <p>
 * One side effect is since the state machine is stateless, we can not get current state from State Machine.
 */
public class GXStateMachineImpl<S, E, C> implements GXStateMachine<S, E, C> {
    private final Map<S, GXState<S, E, C>> stateMap;

    private String machineId;

    private boolean ready;

    public GXStateMachineImpl(Map<S, GXState<S, E, C>> stateMap) {
        this.stateMap = stateMap;
    }

    @Override
    public S fireEvent(S sourceStateId, E event, C ctx) {
        isReady();
        GXTransition<S, E, C> transition = routeTransition(sourceStateId, event, ctx);

        if (transition == null) {
            GXDebugger.debug("There is no Transition for " + event);
            return sourceStateId;
        }

        return transition.transit(ctx).getId();
    }

    private GXTransition<S, E, C> routeTransition(S sourceStateId, E event, C ctx) {
        GXState<S, E, C> sourceState = getState(sourceStateId);

        List<GXTransition<S, E, C>> transitions = sourceState.getEventTransitions(event);

        if (transitions == null || transitions.isEmpty()) {
            return null;
        }

        GXTransition<S, E, C> transit = null;
        for (GXTransition<S, E, C> transition : transitions) {
            if (transition.getCondition() == null) {
                transit = transition;
            } else if (transition.getCondition().isSatisfied(ctx)) {
                transit = transition;
                break;
            }
        }

        return transit;
    }

    private GXState<S, E, C> getState(S currentStateId) {
        GXState<S, E, C> state = GXStateHelper.getState(stateMap, currentStateId);
        if (Objects.isNull(state)) {
            showStateMachine();
            throw new GXStateMachineException(currentStateId + " is not found, please check state machine");
        }
        return state;
    }

    private void isReady() {
        if (!ready) {
            throw new GXStateMachineException("State machine is not built yet, can not work");
        }
    }

    @Override
    public String accept(GXVisitor visitor) {
        StringBuilder sb = new StringBuilder();
        sb.append(visitor.visitOnEntry(this));
        for (GXState<S, E, C> state : stateMap.values()) {
            sb.append(state.accept(visitor));
        }
        sb.append(visitor.visitOnExit(this));
        return sb.toString();
    }

    @Override
    public void showStateMachine() {
        GXSysOutVisitor sysOutVisitor = new GXSysOutVisitor();
        accept(sysOutVisitor);
    }

    @Override
    public String generatePlantUML() {
        GXPlantUMLVisitor plantUMLVisitor = new GXPlantUMLVisitor();
        return accept(plantUMLVisitor);
    }

    @Override
    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
