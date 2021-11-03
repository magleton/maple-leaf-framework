package cn.maple.statemachine.builder;

/**
 * GXTo
 */
public interface GXTo<S, E, C> {
    /**
     * Build transition event
     *
     * @param event transition event
     * @return On clause builder
     */
    GXOn<S, E, C> on(E event);
}
