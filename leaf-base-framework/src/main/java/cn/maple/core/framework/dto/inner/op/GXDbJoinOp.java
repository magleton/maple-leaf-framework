package cn.maple.core.framework.dto.inner.op;

import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.dto.GXBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXDbJoinOp extends GXBaseDto {
    /**
     * 主表别名
     */
    protected String masterAliasTableName;

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
    protected Object fieldValue;

    public GXDbJoinOp() {
    }

    public GXDbJoinOp(String masterFieldName, String subFieldName) {
        this.masterFieldName = masterFieldName;
        this.joinFieldName = subFieldName;
    }

    public GXDbJoinOp setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
        return this;
    }

    public GXDbJoinOp setMasterAliasTableName(String masterAliasTableName) {
        this.masterAliasTableName = masterAliasTableName;
        return this;
    }

    public GXDbJoinOp setJoinTableNameAlias(String joinTableNameAlias) {
        this.joinTableNameAlias = joinTableNameAlias;
        return this;
    }

    public GXDbJoinOp setMasterFieldName(String masterFieldName) {
        this.masterFieldName = masterFieldName;
        return this;
    }

    public GXDbJoinOp setJoinFieldName(String joinFieldName) {
        this.joinFieldName = joinFieldName;
        return this;
    }

    protected String getOp() {
        return "=";
    }

    public String opString() {
        if (CharSequenceUtil.isNotEmpty(masterAliasTableName) && CharSequenceUtil.isNotEmpty(masterFieldName)) {
            masterFieldName = CharSequenceUtil.format("{}.{}", masterAliasTableName, masterFieldName);
        }
        if (CharSequenceUtil.isNotEmpty(joinTableNameAlias) && CharSequenceUtil.isNotEmpty(joinFieldName)) {
            joinFieldName = CharSequenceUtil.format("{}.{}", joinTableNameAlias, joinFieldName);
        }
        if (Objects.nonNull(fieldValue) && CharSequenceUtil.isNotEmpty(masterFieldName) && CharSequenceUtil.isNotEmpty(masterAliasTableName) && CharSequenceUtil.contains(masterFieldName, masterAliasTableName)) {
            return masterFieldName + getOp() + fieldValue;
        } else if (Objects.nonNull(fieldValue) && CharSequenceUtil.isNotEmpty(joinFieldName) && CharSequenceUtil.isNotEmpty(joinTableNameAlias) && CharSequenceUtil.contains(joinFieldName, joinTableNameAlias)) {
            return joinFieldName + getOp() + fieldValue;
        }
        return masterFieldName + getOp() + joinFieldName;
    }
}
