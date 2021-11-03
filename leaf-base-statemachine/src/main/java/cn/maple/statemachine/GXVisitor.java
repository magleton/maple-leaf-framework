package cn.maple.statemachine;

/**
 * GXVisitor
 */
public interface GXVisitor {
    /**
     * 分隔符
     */
    char LF = '\n';

    /**
     * @param visitable the element to be visited.
     * @return String
     */
    String visitOnEntry(GXStateMachine<?, ?, ?> visitable);

    /**
     * @param visitable the element to be visited.
     * @return String
     */
    String visitOnExit(GXStateMachine<?, ?, ?> visitable);

    /**
     * @param visitable the element to be visited.
     * @return String
     */
    String visitOnEntry(GXState<?, ?, ?> visitable);

    /**
     * @param visitable the element to be visited.
     * @return String
     */
    String visitOnExit(GXState<?, ?, ?> visitable);
}
