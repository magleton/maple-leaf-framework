package cn.maple.core.framework.dto.inner.op;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class GXDbJoinEQ extends GXDbJoinOp {
    public GXDbJoinEQ(String masterFieldName, String joinFieldName) {
        super(masterFieldName, joinFieldName);
    }

    @Override
    protected String getOp() {
        return "=";
    }
}
