package cn.maple.statemachine.builder;

/**
 * GXFrom
 */
public interface GXFrom<S, E, C> {
    /**
     * Build transition target state and return to clause builder
     *
     * @param stateId id of state
     * @return To clause builder
     */
    GXTo<S, E, C> to(S stateId);
}
