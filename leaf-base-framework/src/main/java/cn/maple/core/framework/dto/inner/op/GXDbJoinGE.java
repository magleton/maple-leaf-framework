package cn.maple.core.framework.dto.inner.op;

public class GXDbJoinGE extends GXDbJoinOp {
    public GXDbJoinGE() {
        super();
    }

    public GXDbJoinGE(String masterFieldName, String subFieldName) {
        super(masterFieldName, subFieldName);
    }

    @Override
    protected String getOp() {
        return ">";
    }
}
