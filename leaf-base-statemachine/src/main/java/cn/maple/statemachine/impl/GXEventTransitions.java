package cn.maple.statemachine.impl;

import cn.maple.statemachine.GXTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * GXEventTransitions
 * <p>
 * 同一个Event可以触发多个Transitions
 */
public class GXEventTransitions<S, E, C> {
    private final HashMap<E, List<GXTransition<S, E, C>>> eventTransitions;

    public GXEventTransitions() {
        eventTransitions = new HashMap<>();
    }

    public void put(E event, GXTransition<S, E, C> transition) {
        if (eventTransitions.get(event) == null) {
            List<GXTransition<S, E, C>> transitions = new ArrayList<>();
            transitions.add(transition);
            eventTransitions.put(event, transitions);
        } else {
            List<GXTransition<S, E, C>> existingTransitions = eventTransitions.get(event);
            verify(existingTransitions, transition);
            existingTransitions.add(transition);
        }
    }

    /**
     * Per one source and target state, there is only one transition is allowed
     *
     * @param existingTransitions 存在的转换器
     * @param newTransition       新的转换器
     */
    private void verify(List<GXTransition<S, E, C>> existingTransitions, GXTransition<S, E, C> newTransition) {
        for (GXTransition<S, E, C> transition : existingTransitions) {
            if (transition.equals(newTransition)) {
                throw new GXStateMachineException(transition + " already Exist, you can not add another one");
            }
        }
    }

    public List<GXTransition<S, E, C>> get(E event) {
        return eventTransitions.get(event);
    }

    public List<GXTransition<S, E, C>> allTransitions() {
        List<GXTransition<S, E, C>> allTransitions = new ArrayList<>();
        for (List<GXTransition<S, E, C>> transitions : eventTransitions.values()) {
            allTransitions.addAll(transitions);
        }
        return allTransitions;
    }
}
