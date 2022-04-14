package cn.maple.core.framework.dto.inner.op;

public class GXDbJoinGE extends GXDbJoinOp {
    public GXDbJoinGE() {
        super();
    }

    public GXDbJoinGE(String masterFieldName, String joinFieldName) {
        super(masterFieldName, joinFieldName);
    }

    @Override
    protected String getOp() {
        return ">";
    }
}
