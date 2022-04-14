package cn.maple.core.framework.dto.inner.op;

@SuppressWarnings("all")
public class GXDbJoinEQ extends GXDbJoinOp {
    public GXDbJoinEQ(String masterFieldName, String joinFieldName) {
        super(masterFieldName, joinFieldName);
    }

    @Override
    protected String getOp() {
        return "=";
    }
}
