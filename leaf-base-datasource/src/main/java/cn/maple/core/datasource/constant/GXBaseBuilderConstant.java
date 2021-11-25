package cn.maple.core.datasource.constant;

public class GXBaseBuilderConstant {
    /**
     * 搜索条件的名字
     */
    public static final String SEARCH_CONDITION_NAME = "searchCondition";

    /**
     * 左糊查询条件
     */
    public static final String LEFT_LIKE = " like '%{}'";

    /**
     * 后模糊查询条件
     */
    public static final String RIGHT_LIKE = " like '{}%'";

    /**
     * 全模糊查询
     */
    public static final String FULL_LIKE = " like '%{}%'";

    /**
     * 字符串相等
     */
    public static final String STR_EQ = " = '{}'";

    /**
     * 字符串相等
     */
    public static final String STR_NOT_EQ = " != '{}'";

    /**
     * 数字相等
     */
    public static final String NUMBER_EQ = " = {}";

    /**
     * 数字不相等
     */
    public static final String NUMBER_NOT_EQ = " != {}";

    /**
     * 数字小于
     */
    public static final String NUMBER_LT = " < {}";

    /**
     * 数字小于等于
     */
    public static final String NUMBER_LE = " <= {}";

    /**
     * 数字大于
     */
    public static final String NUMBER_GT = " > {}";

    /**
     * 数字大于等于
     */
    public static final String NUMBER_GE = " >= {}";

    /**
     * IN条件
     */
    public static final String IN = " IN ({})";

    /**
     * NOT IN条件
     */
    public static final String NOT_IN = " NOT IN ({})";

    /**
     * 标识查询条件是一个函数类型 eg : JSON_OVERLAPS(items->'$.zipcode', CAST('[94536]' AS JSON))
     */
    public static final String T_FUNC_MARK = "T_FUNC";

    private GXBaseBuilderConstant() {
    }
}
