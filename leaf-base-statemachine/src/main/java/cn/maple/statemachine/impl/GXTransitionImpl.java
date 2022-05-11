package cn.maple.statemachine.impl;

import cn.maple.statemachine.GXAction;
import cn.maple.statemachine.GXCondition;
import cn.maple.statemachine.GXState;
import cn.maple.statemachine.GXTransition;

/**
 * GXTransitionImpl
 * <p>
 * This should be designed to be immutable, so that there is no thread-safe risk
 */
public class GXTransitionImpl<S, E, C> implements GXTransition<S, E, C> {

    private GXState<S, E, C> source;

    private GXState<S, E, C> target;

    private E event;

    private GXCondition<C> condition;

    private GXAction<S, E, C> action;

    private GXTransitionType type = GXTransitionType.EXTERNAL;

    @Override
    public GXState<S, E, C> getSource() {
        return source;
    }

    @Override
    public void setSource(GXState<S, E, C> state) {
        this.source = state;
    }

    @Override
    public E getEvent() {
        return this.event;
    }

    @Override
    public void setEvent(E event) {
        this.event = event;
    }

    @Override
    public void setType(GXTransitionType type) {
        this.type = type;
    }

    @Override
    public GXState<S, E, C> getTarget() {
        return this.target;
    }

    @Override
    public void setTarget(GXState<S, E, C> target) {
        this.target = target;
    }

    @Override
    public GXCondition<C> getCondition() {
        return this.condition;
    }

    @Override
    public void setCondition(GXCondition<C> condition) {
        this.condition = condition;
    }

    @Override
    public GXAction<S, E, C> getAction() {
        return this.action;
    }

    @Override
    public void setAction(GXAction<S, E, C> action) {
        this.action = action;
    }

    @Override
    public GXState<S, E, C> transit(C ctx, boolean checkCondition) {
        GXDebugger.debug("Do transition: " + this);
        this.verify();
        if (!checkCondition || condition == null || condition.isSatisfied(ctx)) {
            if (action != null) {
                action.execute(source.getId(), target.getId(), event, ctx);
            }
            return target;
        }

        GXDebugger.debug("Condition is not satisfied, stay at the " + source + " state ");
        return source;
    }

    @Override
    public final String toString() {
        return source + "-[" + event.toString() + ", " + type + "]->" + target;
    }

    @Override
    public boolean equals(Object anObject) {
        if (anObject instanceof GXTransition) {
            GXTransition<?, ?, ?> other = (GXTransition<?, ?, ?>) anObject;
            return this.event.equals(other.getEvent())
                    && this.source.equals(other.getSource())
                    && this.target.equals(other.getTarget());
        }
        return false;
    }

    @Override
    public void verify() {
        if (type == GXTransitionType.INTERNAL && source != target) {
            throw new GXStateMachineException(String.format("Internal transition source state '%s' " +
                    "and target state '%s' must be same.", source, target));
        }
    }
}
