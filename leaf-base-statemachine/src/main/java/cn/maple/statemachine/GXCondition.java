package cn.maple.statemachine;

/**
 * GXCondition
 */
public interface GXCondition<C> {
    /**
     * @param context context object
     * @return whether the context satisfied current condition
     */
    boolean isSatisfied(C context);

    /**
     * 条件名字
     *
     * @return String
     */
    default String name() {
        return this.getClass().getSimpleName();
    }
}