package cn.maple.statemachine;

import cn.maple.statemachine.impl.GXStateMachineException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GXStateMachineFactory
 */
@Slf4j
public class GXStateMachineFactory {
    static Map<String /* machineId */, GXStateMachine<?, ?, ?>> stateMachineMap = new ConcurrentHashMap<>();

    private GXStateMachineFactory() {
    }

    public static <S, E, C> void register(GXStateMachine<S, E, C> stateMachine) {
        String machineId = stateMachine.getMachineId();
        if (stateMachineMap.get(machineId) != null) {
            throw new GXStateMachineException("The state machine with id [" + machineId + "] is already built, no need to build again");
        }
        stateMachineMap.put(stateMachine.getMachineId(), stateMachine);
    }

    public static <S, E, C> GXStateMachine<S, E, C> get(String machineId) {
        GXStateMachine<S, E, C> stateMachine = (GXStateMachine<S, E, C>) stateMachineMap.get(machineId);
        if (stateMachine == null) {
            throw new GXStateMachineException("There is no stateMachine instance for " + machineId + ", please build it first");
        }
        return stateMachine;
    }
}
