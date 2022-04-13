package cn.maple.core.framework.dto.inner;

import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.dto.inner.op.GXDbJoinOp;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GXJoinDto {
    /**
     * 表名字
     */
    private String joinTableName;

    /**
     * 表别名
     */
    private String joinTableNameAlias;

    /**
     * 主表别名
     */
    private String masterTableNameAlias;

    /**
     * 链接类型
     */
    private String joinType;

    /**
     * 链接的AND条件
     */
    private List<GXDbJoinOp> and;

    /**
     * 链接的OR条件
     */
    private List<GXDbJoinOp> or;

    public void setAnd(List<GXDbJoinOp> and) {
        and.forEach(op -> {
            if (CharSequenceUtil.isEmpty(op.getMasterAliasTableName())) {
                op.setMasterAliasTableName(masterTableNameAlias);
            }
            if (CharSequenceUtil.isEmpty(op.getJoinTableNameAlias())) {
                op.setJoinTableNameAlias(joinTableNameAlias);
            }
        });
        this.and = and;
    }

    public void setOr(List<GXDbJoinOp> or) {
        or.forEach(op -> {
            if (CharSequenceUtil.isEmpty(op.getMasterAliasTableName())) {
                op.setMasterAliasTableName(masterTableNameAlias);
            }
            if (CharSequenceUtil.isEmpty(op.getJoinTableNameAlias())) {
                op.setJoinTableNameAlias(joinTableNameAlias);
            }
        });
        this.or = or;
    }
}
