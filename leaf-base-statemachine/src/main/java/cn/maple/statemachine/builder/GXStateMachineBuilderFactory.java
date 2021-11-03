package cn.maple.statemachine.builder;

/**
 * GXStateMachineBuilderFactory
 */
public class GXStateMachineBuilderFactory {
    private GXStateMachineBuilderFactory() {
    }

    public static <S, E, C> GXStateMachineBuilder<S, E, C> create() {
        return new GXStateMachineBuilderImpl<>();
    }
}
