package cn.maple.core.datasource.constant;

public class GXBuilderConstant {
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
    public static final String LIKE = " like '%{}%'";

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

    /**
     * 左连接类型
     */
    public static final String LEFT_JOIN_TYPE = "left";

    /**
     * 右链接类型
     */
    public static final String RIGHT_JOIN_TYPE = "right";

    /**
     * 内链接类型
     */
    public static final String INNER_JOIN_TYPE = "inner";

    /**
     * JOIN的ON模板字符串
     */
    public static final String JOIN_ON_STR = "{} {} ON {}";

    /**
     * 更新字段时需要移除的json字段的前缀
     * <pre>
     * {@code
     * final Table<String, String, Object> extData = HashBasedTable.create();
     * extData.put("ext", "name", "jack");
     * extData.put("ext", "address", "四川成都");
     * extData.put("ext", GXBuilderConstant.REMOVE_JSON_FIELD_PREFIX_FLAG + "salary" , "");
     * final Dict data = Dict.create().set("category_name", "打折商品").set("ext", extData);
     * Table<String , String , Object> condition = HashBasedTable.create();
     * condition.put("category_id" ,  GXBuilderConstant.STR_EQ , 11111);
     * updateFieldByCondition("s_category", data, condition);
     * }
     * </pre>
     */
    public static final String REMOVE_JSON_FIELD_PREFIX_FLAG = "-";

    /**
     * 数据库删除标记的字段名字
     */
    public static final String DB_DELETED_FLAG_FIELD_NAME = "is_deleted";

    private GXBuilderConstant() {
    }
}
