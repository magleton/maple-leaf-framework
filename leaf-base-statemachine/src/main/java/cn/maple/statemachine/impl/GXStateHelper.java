package cn.maple.statemachine.impl;

import cn.maple.statemachine.GXState;

import java.util.Map;

/**
 * GXStateHelper
 */
public class GXStateHelper {
    private GXStateHelper() {
    }

    public static <S, E, C> GXState<S, E, C> getState(Map<S, GXState<S, E, C>> stateMap, S stateId) {
        GXState<S, E, C> state = stateMap.get(stateId);
        if (state == null) {
            state = new GXStateImpl<>(stateId);
            stateMap.put(stateId, state);
        }
        return state;
    }
}
