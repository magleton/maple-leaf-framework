package cn.maple.statemachine;

/**
 * GXStateMachine
 *
 * @param <S> the type of state
 * @param <E> the type of event
 * @param <C> the user defined context
 */
public interface GXStateMachine<S, E, C> extends GXVisitable {
    /**
     * Send an event {@code E} to the state machine.
     *
     * @param sourceState the source state
     * @param event       the event to send
     * @param ctx         the user defined context
     * @return the target state
     */
    S fireEvent(S sourceState, E event, C ctx);

    /**
     * MachineId is the identifier for a State Machine
     *
     * @return String
     */
    String getMachineId();

    /**
     * Use visitor pattern to display the structure of the state machine
     */
    void showStateMachine();

    /**
     * 生成UML图
     *
     * @return UML文本字符串
     */
    String generatePlantUML();
}
