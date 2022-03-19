package cn.maple.core.framework.constant;

public class GXBuilderConstant {
    /**
     * 搜索条件的名字
     */
    public static final String SEARCH_CONDITION_NAME = "searchCondition";

    /**
     * 数字相等
     */
    public static final String EQ = " = {}";

    /**
     * 数字不相等
     */
    public static final String NOT_EQ = " != {}";

    /**
     * 数字小于
     */
    public static final String LT = " < {}";

    /**
     * 数字小于等于
     */
    public static final String LE = " <= {}";

    /**
     * 数字大于
     */
    public static final String GT = " > {}";

    /**
     * 数字大于等于
     */
    public static final String GE = " >= {}";

    /**
     * IN条件
     */
    public static final String IN = " IN ({})";

    /**
     * NOT IN条件
     */
    public static final String NOT_IN = " NOT IN ({})";

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
    public static final String STR_EQ = "STR_ = '{}'";

    /**
     * 字符串相等
     */
    public static final String STR_NOT_EQ = "STR_ != '{}'";

    /**
     * IN条件
     */
    public static final String STR_IN = "STR_ IN ({})";

    /**
     * NOT IN条件
     */
    public static final String STR_NOT_IN = "STR_ NOT IN ({})";

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
     * 数据库中删除标记的字段名字
     */
    public static final String DELETED_FLAG_FIELD_NAME = "is_deleted";

    /**
     * 排除删除条件的标识
     * 如果额外传输了该字段, 则查询SQL中会查询所有满足条件的数据 , 无论删除与否
     */
    public static final String EXCLUSION_DELETED_CONDITION_FLAG = "exclusion_deleted_condition";

    /**
     * 排除删除条件的标识的值
     * EXCLUSION_DELETED_CONDITION_FLAG 标识字段的值
     */
    public static final String EXCLUSION_DELETED_CONDITION_VALUE = "";

    /**
     * 数据库主键字段的默认名字
     */
    public static final String DEFAULT_ID_NAME = "id";

    /**
     * 数据库JSON查询字符串模板表达式
     */
    public static final String JSON_SEARCH_EXPRESSION_TEMPLATE = "{}->'{}' , CAST('[{}]' as JSON)";

    /**
     * JOIN条件值开头的标识
     * eg:
     * ON a.id = b.aid and type = 1
     *
     */
    public static final String JOIN_ON_START_WITH_MARKER = "&";

    private GXBuilderConstant() {
    }
}
