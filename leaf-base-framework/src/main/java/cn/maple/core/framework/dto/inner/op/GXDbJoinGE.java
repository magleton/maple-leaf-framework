package cn.maple.core.framework.dto.inner.op;

@SuppressWarnings("all")
public class GXDbJoinGE extends GXDbJoinOp {
    public GXDbJoinGE(String masterFieldName, String joinFieldName) {
        super(masterFieldName, joinFieldName);
    }

    @Override
    protected String getOp() {
        return ">";
    }
}
