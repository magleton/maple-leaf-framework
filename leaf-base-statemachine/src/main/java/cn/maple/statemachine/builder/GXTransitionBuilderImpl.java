package cn.maple.statemachine.builder;

import cn.maple.statemachine.GXAction;
import cn.maple.statemachine.GXCondition;
import cn.maple.statemachine.GXState;
import cn.maple.statemachine.GXTransition;
import cn.maple.statemachine.impl.GXStateHelper;
import cn.maple.statemachine.impl.GXTransitionType;

import java.util.Map;

/**
 * GXTransitionBuilderImpl
 */
class GXTransitionBuilderImpl<S, E, C> implements GXExternalTransitionBuilder<S, E, C>, GXInternalTransitionBuilder<S, E, C>, GXFrom<S, E, C>, GXOn<S, E, C>, GXTo<S, E, C> {
    /**
     * 状态缓存
     */
    final Map<S, GXState<S, E, C>> stateMap;

    /**
     * 转换类型
     */
    final GXTransitionType transitionType;

    /**
     * 目标状态
     */
    protected GXState<S, E, C> target;

    /**
     * 源状态
     */
    private GXState<S, E, C> source;

    /**
     * 转换对象
     */
    private GXTransition<S, E, C> transition;

    public GXTransitionBuilderImpl(Map<S, GXState<S, E, C>> stateMap, GXTransitionType transitionType) {
        this.stateMap = stateMap;
        this.transitionType = transitionType;
    }

    @Override
    public GXFrom<S, E, C> from(S stateId) {
        source = GXStateHelper.getState(stateMap, stateId);
        return this;
    }

    @Override
    public GXTo<S, E, C> to(S stateId) {
        target = GXStateHelper.getState(stateMap, stateId);
        return this;
    }

    @Override
    public GXTo<S, E, C> within(S stateId) {
        source = target = GXStateHelper.getState(stateMap, stateId);
        return this;
    }

    @Override
    public GXWhen<S, E, C> when(GXCondition<C> condition) {
        transition.setCondition(condition);
        return this;
    }

    @Override
    public GXOn<S, E, C> on(E event) {
        transition = source.addTransition(event, target, transitionType);
        return this;
    }

    @Override
    public void perform(GXAction<S, E, C> action) {
        transition.setAction(action);
    }
}
