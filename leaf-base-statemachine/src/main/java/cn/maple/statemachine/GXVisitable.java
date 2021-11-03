package cn.maple.statemachine;

/**
 * GXVisitable
 */
public interface GXVisitable {
    String accept(final GXVisitor visitor);
}
