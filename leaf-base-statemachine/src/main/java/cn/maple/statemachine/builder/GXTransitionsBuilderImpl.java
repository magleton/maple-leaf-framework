package cn.maple.statemachine.builder;

import cn.maple.statemachine.GXAction;
import cn.maple.statemachine.GXCondition;
import cn.maple.statemachine.GXState;
import cn.maple.statemachine.GXTransition;
import cn.maple.statemachine.impl.GXStateHelper;
import cn.maple.statemachine.impl.GXTransitionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GXTransitionsBuilderImpl
 */
public class GXTransitionsBuilderImpl<S, E, C> extends GXTransitionBuilderImpl<S, E, C> implements GXExternalTransitionsBuilder<S, E, C> {
    /**
     * This is for fromAmong where multiple sources can be configured to point to one target
     */
    private final List<GXState<S, E, C>> sources = new ArrayList<>();

    private final List<GXTransition<S, E, C>> transitions = new ArrayList<>();

    public GXTransitionsBuilderImpl(Map<S, GXState<S, E, C>> stateMap, GXTransitionType transitionType) {
        super(stateMap, transitionType);
    }

    @SafeVarargs
    @Override
    public final GXFrom<S, E, C> fromAmong(S... stateIds) {
        for (S stateId : stateIds) {
            sources.add(GXStateHelper.getState(super.stateMap, stateId));
        }
        return this;
    }

    @Override
    public GXOn<S, E, C> on(E event) {
        for (GXState<S, E, C> source : sources) {
            GXTransition<S, E, C> transition = source.addTransition(event, super.target, super.transitionType);
            transitions.add(transition);
        }
        return this;
    }

    @Override
    public GXWhen<S, E, C> when(GXCondition<C> condition) {
        for (GXTransition<S, E, C> transition : transitions) {
            transition.setCondition(condition);
        }
        return this;
    }

    @Override
    public void perform(GXAction<S, E, C> action) {
        for (GXTransition<S, E, C> transition : transitions) {
            transition.setAction(action);
        }
    }
}
