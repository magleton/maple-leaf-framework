package cn.maple.statemachine;

/**
 * Generic strategy interface used by a state machine to respond
 * events by executing an {@code GXAction} with a {@link GXStateContext}.
 */
public interface GXAction<S, E, C> {
//    /**
//     * Execute action with a {@link StateContext}.
//     *
//     * @param context the state context
//     */
//    void execute(StateContext<S, E> context);

    void execute(S from, S to, E event, C context);
}
