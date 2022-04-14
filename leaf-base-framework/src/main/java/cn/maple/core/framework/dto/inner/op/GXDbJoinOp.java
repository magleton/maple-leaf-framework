package cn.maple.core.framework.dto.inner.op;

import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.dto.GXBaseDto;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class GXDbJoinOp extends GXBaseDto {
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

    /**
     * 字段的值  用于查询固定指端的场景
     */
    protected transient Object fieldValue;

    public GXDbJoinOp() {

    }

    public GXDbJoinOp(String masterFieldName, String subFieldName) {
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

    protected String getOp() {
        return "=";
    }

    public String opString() {
        if (CharSequenceUtil.isNotEmpty(masterTableNameAlias) && CharSequenceUtil.isNotEmpty(masterFieldName)) {
            masterFieldName = CharSequenceUtil.format("{}.{}", masterTableNameAlias, masterFieldName);
        }
        if (CharSequenceUtil.isNotEmpty(joinTableNameAlias) && CharSequenceUtil.isNotEmpty(joinFieldName)) {
            joinFieldName = CharSequenceUtil.format("{}.{}", joinTableNameAlias, joinFieldName);
        }
        return masterFieldName + getOp() + joinFieldName;
    }
}
