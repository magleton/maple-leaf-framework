package cn.maple.statemachine.builder;

/**
 * ExternalTransitionBuilder
 */
public interface GXExternalTransitionBuilder<S, E, C> {
    /**
     * Build transition source state.
     *
     * @param stateId id of state
     * @return from clause builder
     */
    GXFrom<S, E, C> from(S stateId);
}
