package cn.maple.statemachine.builder;

/**
 * GXInternalTransitionBuilder
 */
public interface GXInternalTransitionBuilder<S, E, C> {
    /**
     * Build a internal transition
     *
     * @param stateId id of transition
     * @return To clause builder
     */
    GXTo<S, E, C> within(S stateId);
}
