package cn.maple.statemachine.builder;

/**
 * ExternalTransitionsBuilder
 * <p>
 * This builder is for multiple transitions, currently only support multiple sources <----> one target
 */
public interface GXExternalTransitionsBuilder<S, E, C> {
    GXFrom<S, E, C> fromAmong(S... stateIds);
}
