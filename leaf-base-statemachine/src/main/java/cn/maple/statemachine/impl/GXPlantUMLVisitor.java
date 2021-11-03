package cn.maple.statemachine.impl;

import cn.maple.statemachine.GXState;
import cn.maple.statemachine.GXStateMachine;
import cn.maple.statemachine.GXTransition;
import cn.maple.statemachine.GXVisitor;

import java.util.Collection;

/**
 * GXPlantUMLVisitor
 */
public class GXPlantUMLVisitor implements GXVisitor {
    /**
     * Since the state machine is stateless, there is no initial state.
     * <p>
     * You have to add "[*] -> initialState" to mark it as a state machine diagram.
     * otherwise it will be recognized as a sequence diagram.
     *
     * @param visitable the element to be visited.
     * @return
     */
    @Override
    public String visitOnEntry(GXStateMachine<?, ?, ?> visitable) {
        return "@startuml" + LF;
    }

    @Override
    public String visitOnExit(GXStateMachine<?, ?, ?> visitable) {
        return "@enduml";
    }

    @Override
    public String visitOnEntry(GXState<?, ?, ?> state) {
        StringBuilder sb = new StringBuilder();
        Collection<? extends GXTransition<?, ?, ?>> allTransitions = state.getAllTransitions();
        for (GXTransition<?, ?, ?> transition : allTransitions) {
            sb.append(transition.getSource().getId())
                    .append(" --> ")
                    .append(transition.getTarget().getId())
                    .append(" : ")
                    .append(transition.getEvent())
                    .append(LF);
        }
        return sb.toString();
    }

    @Override
    public String visitOnExit(GXState<?, ?, ?> state) {
        return "";
    }
}
