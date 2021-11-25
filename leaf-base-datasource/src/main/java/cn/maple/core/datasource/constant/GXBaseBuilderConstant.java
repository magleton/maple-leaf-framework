package cn.maple.core.datasource.constant;

import cn.maple.core.framework.annotation.GXFieldComment;

public class GXBaseBuilderConstant {
    @GXFieldComment(zhDesc = "搜索条件的名字")
    public static final String SEARCH_CONDITION_NAME = "searchCondition";

    @GXFieldComment(zhDesc = "左糊查询条件")
    public static final String LEFT_LIKE = " like '%{}'";

    @GXFieldComment(zhDesc = "后模糊查询条件")
    public static final String RIGHT_LIKE = " like '{}%'";

    @GXFieldComment(zhDesc = "全模糊查询")
    public static final String FULL_LIKE = " like '%{}%'";

    @GXFieldComment(zhDesc = "字符串相等")
    public static final String STR_EQ = " = '{}'";

    @GXFieldComment(zhDesc = "字符串相等")
    public static final String STR_NOT_EQ = " != '{}'";

    @GXFieldComment(zhDesc = "数字相等")
    public static final String NUMBER_EQ = " = {}";

    @GXFieldComment(zhDesc = "数字不相等")
    public static final String NUMBER_NOT_EQ = " != {}";

    @GXFieldComment(zhDesc = "数字小于")
    public static final String NUMBER_LT = " < {}";

    @GXFieldComment(zhDesc = "数字小于等于")
    public static final String NUMBER_LE = " <= {}";

    @GXFieldComment(zhDesc = "数字大于")
    public static final String NUMBER_GT = " > {}";

    @GXFieldComment(zhDesc = "数字大于等于")
    public static final String NUMBER_GE = " >= {}";

    @GXFieldComment(zhDesc = "IN条件")
    public static final String IN = " IN ({})";

    @GXFieldComment(zhDesc = "NOT IN条件")
    public static final String NOT_IN = " NOT IN ({})";

    @GXFieldComment(zhDesc = "标识查询条件是一个函数类型 eg : JSON_OVERLAPS(items->'$.zipcode', CAST('[94536]' AS JSON))")
    public static final String T_FUNC_MARK = "T_FUNC";

    private GXBaseBuilderConstant() {
    }
}
