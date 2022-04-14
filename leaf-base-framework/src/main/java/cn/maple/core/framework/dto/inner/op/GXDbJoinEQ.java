package cn.maple.core.framework.dto.inner.op;

public class GXDbJoinEQ extends GXDbJoinOp {
    public GXDbJoinEQ() {
        super();
    }

    public GXDbJoinEQ(String masterFieldName, String joinFieldName) {
        super(masterFieldName, joinFieldName);
    }

    @Override
    protected String getOp() {
        return "=";
    }
}
