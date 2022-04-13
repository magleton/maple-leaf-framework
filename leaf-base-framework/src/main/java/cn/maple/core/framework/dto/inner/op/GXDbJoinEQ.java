package cn.maple.core.framework.dto.inner.op;

public class GXDbJoinEQ extends GXDbJoinOp {
    public GXDbJoinEQ() {
        super();
    }

    public GXDbJoinEQ(String masterFieldName, String subFieldName) {
        super(masterFieldName, subFieldName);
    }

    @Override
    protected String getOp() {
        return "=";
    }
}
