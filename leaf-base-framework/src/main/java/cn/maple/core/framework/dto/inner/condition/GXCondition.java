package cn.maple.core.framework.dto.inner.condition;

import cn.hutool.core.text.CharSequenceUtil;

import java.io.Serializable;

public abstract class GXCondition<T> implements Serializable {
    protected final String tableNameAlias;

    /**
     * 可以是一个具体的字段名字  goods_name
     * <p>
     * 也可以是一个函数表达式  concat(g_goods.goods_name , '-' , g_goods.goods_sn)
     */
    protected final String fieldExpression;

    @SuppressWarnings("all")
    protected Object value;

    protected GXCondition(String fieldExpression, Object value) {
        this("", fieldExpression, value);
    }

    protected GXCondition(String tableNameAlias, String fieldExpression, Object value) {
        this.tableNameAlias = tableNameAlias;
        this.fieldExpression = fieldExpression;
        this.value = value;
    }

    public abstract String getOp();

    public String whereString() {
        String opStr = getOp();
        if (CharSequenceUtil.isEmpty(opStr)) {
            return "";
        }
        if (CharSequenceUtil.isEmpty(tableNameAlias)) {
            return CharSequenceUtil.format("{} {} {}", fieldExpression, opStr, getFieldValue());
        }
        return CharSequenceUtil.format("{}.{} {} {}", tableNameAlias, CharSequenceUtil.toUnderlineCase(fieldExpression), opStr, getFieldValue());
    }

    public String getFieldExpression() {
        return CharSequenceUtil.toUnderlineCase(fieldExpression);
    }

    public abstract T getFieldValue();
}
