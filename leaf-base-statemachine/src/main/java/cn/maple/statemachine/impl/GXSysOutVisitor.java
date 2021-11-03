package cn.maple.statemachine.impl;

import cn.maple.statemachine.GXState;
import cn.maple.statemachine.GXStateMachine;
import cn.maple.statemachine.GXTransition;
import cn.maple.statemachine.GXVisitor;
import lombok.extern.slf4j.Slf4j;

/**
 * GXSysOutVisitor
 */
@Slf4j
public class GXSysOutVisitor implements GXVisitor {
    @Override
    public String visitOnEntry(GXStateMachine<?, ?, ?> stateMachine) {
        String entry = "-----StateMachine:" + stateMachine.getMachineId() + "-------";
        log.info(entry);
        return entry;
    }

    @Override
    public String visitOnExit(GXStateMachine<?, ?, ?> stateMachine) {
        String exit = "------------------------";
        log.info(exit);
        return exit;
    }

    @Override
    public String visitOnEntry(GXState<?, ?, ?> state) {
        StringBuilder sb = new StringBuilder();
        String stateStr = "State:" + state.getId();
        sb.append(stateStr).append(LF);
        log.info(stateStr);
        for (GXTransition<?, ?, ?> transition : state.getAllTransitions()) {
            String transitionStr = "    Transition:" + transition;
            sb.append(transitionStr).append(LF);
            log.info(transitionStr);
        }
        return sb.toString();
    }

    @Override
    public String visitOnExit(GXState<?, ?, ?> visitable) {
        return "";
    }
}
