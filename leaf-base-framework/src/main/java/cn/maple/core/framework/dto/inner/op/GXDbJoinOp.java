package cn.maple.core.framework.dto.inner.op;

import cn.hutool.core.text.CharSequenceUtil;

public abstract class GXDbJoinOp {
    /**
     * 主表别名
     */
    protected String masterTableNameAlias;

    /**
     * 主表字段
     */
    protected String masterFieldName;

    /**
     * 子表别名
     */
    protected String joinTableNameAlias;

    /**
     * 子表字段
     */
    protected String joinFieldName;

    protected GXDbJoinOp() {

    }

    protected GXDbJoinOp(String masterFieldName, String subFieldName) {
        this.masterFieldName = masterFieldName;
        this.joinFieldName = subFieldName;
    }

    public String getMasterTableNameAlias() {
        return masterTableNameAlias;
    }

    public void setMasterTableNameAlias(String masterTableNameAlias) {
        this.masterTableNameAlias = masterTableNameAlias;
    }

    public String getJoinTableNameAlias() {
        return joinTableNameAlias;
    }

    public void setJoinTableNameAlias(String joinTableNameAlias) {
        this.joinTableNameAlias = joinTableNameAlias;
    }

    abstract String getOp();

    public String opString() {
        if (CharSequenceUtil.contains(masterFieldName, ".")) {
            masterFieldName = masterFieldName.split("\\.")[1];
        }
        if (CharSequenceUtil.contains(joinFieldName, ".")) {
            joinFieldName = joinFieldName.split("\\.")[1];
        }
        if (CharSequenceUtil.isNotEmpty(masterTableNameAlias) && CharSequenceUtil.isNotEmpty(masterFieldName)) {
            masterFieldName = CharSequenceUtil.format("{}.{}", masterTableNameAlias, masterFieldName);
        }
        if (CharSequenceUtil.isNotEmpty(joinTableNameAlias) && CharSequenceUtil.isNotEmpty(joinFieldName)) {
            joinFieldName = CharSequenceUtil.format("{}.{}", joinTableNameAlias, joinFieldName);
        }
        return masterFieldName + getOp() + joinFieldName;
    }
}
