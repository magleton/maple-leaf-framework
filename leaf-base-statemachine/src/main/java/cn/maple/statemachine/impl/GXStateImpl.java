package cn.maple.statemachine.impl;

import cn.maple.statemachine.GXState;
import cn.maple.statemachine.GXTransition;
import cn.maple.statemachine.GXVisitor;

import java.util.Collection;
import java.util.List;

/**
 * GXStateImpl
 *
 */
public class GXStateImpl<S, E, C> implements GXState<S, E, C> {
    /**
     * 状态ID
     */
    protected final S stateId;

    /**
     * 时间转换
     */
    private final GXEventTransitions<S, E, C> eventTransitions = new GXEventTransitions<>();

    GXStateImpl(S stateId) {
        this.stateId = stateId;
    }

    @Override
    public GXTransition<S, E, C> addTransition(E event, GXState<S, E, C> target, GXTransitionType transitionType) {
        GXTransition<S, E, C> newTransition = new GXTransitionImpl<>();
        newTransition.setSource(this);
        newTransition.setTarget(target);
        newTransition.setEvent(event);
        newTransition.setType(transitionType);
        GXDebugger.debug("Begin to add new transition: " + newTransition);
        eventTransitions.put(event, newTransition);
        return newTransition;
    }

    @Override
    public List<GXTransition<S, E, C>> getEventTransitions(E event) {
        return eventTransitions.get(event);
    }

    @Override
    public Collection<GXTransition<S, E, C>> getAllTransitions() {
        return eventTransitions.allTransitions();
    }

    @Override
    public S getId() {
        return stateId;
    }

    @Override
    public String accept(GXVisitor visitor) {
        String entry = visitor.visitOnEntry(this);
        String exit = visitor.visitOnExit(this);
        return entry + exit;
    }

    @Override
    public boolean equals(Object anObject) {
        if (anObject instanceof GXState) {
            GXState<?, ?, ?> other = (GXState<?, ?, ?>) anObject;
            return this.stateId.equals(other.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        return stateId.toString();
    }
}
