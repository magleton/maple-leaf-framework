package cn.maple.core.framework.dto.inner.op;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class GXDbJoinGE extends GXDbJoinOp {
    public GXDbJoinGE(String masterFieldName, String joinFieldName) {
        super(masterFieldName, joinFieldName);
    }

    @Override
    protected String getOp() {
        return ">";
    }
}
