package com.geoxus.core.common.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class GXBaseBuilderConstants {
    @GXFieldCommentAnnotation(zhDesc = "搜索条件的名字")
    public static final String SEARCH_CONDITION_NAME = "searchCondition";

    @GXFieldCommentAnnotation(zhDesc = "模型标识的名字")
    public static final String MODEL_IDENTIFICATION_NAME = "model_identification";

    @GXFieldCommentAnnotation(zhDesc = "左糊查询条件")
    public static final String LEFT_LIKE = " like '%{}'";

    @GXFieldCommentAnnotation(zhDesc = "后模糊查询条件")
    public static final String RIGHT_LIKE = " like '{}%'";

    @GXFieldCommentAnnotation(zhDesc = "全模糊查询")
    public static final String FULL_LIKE = " like '%{}%'";

    @GXFieldCommentAnnotation(zhDesc = "字符串相等")
    public static final String STR_EQ = " = '{}'";

    @GXFieldCommentAnnotation(zhDesc = "字符串相等")
    public static final String STR_NOT_EQ = " != '{}'";

    @GXFieldCommentAnnotation(zhDesc = "数字相等")
    public static final String NUMBER_EQ = " = {}";

    @GXFieldCommentAnnotation(zhDesc = "数字不相等")
    public static final String NUMBER_NOT_EQ = " != {}";

    @GXFieldCommentAnnotation(zhDesc = "数字小于")
    public static final String NUMBER_LT = " < {}";

    @GXFieldCommentAnnotation(zhDesc = "数字小于等于")
    public static final String NUMBER_LE = " <= {}";

    @GXFieldCommentAnnotation(zhDesc = "数字大于")
    public static final String NUMBER_GT = " > {}";

    @GXFieldCommentAnnotation(zhDesc = "数字大于等于")
    public static final String NUMBER_GE = " >= {}";

    @GXFieldCommentAnnotation(zhDesc = "时间区间 开始结束都带等号")
    public static final String TIME_RANGE_WITH_EQ = "{} >= {} AND {} <= {}";

    @GXFieldCommentAnnotation(zhDesc = "时间区间 开始带等号")
    public static final String TIME_RANGE_WITH_START_EQ = "{} >= {} AND {} < {}";

    @GXFieldCommentAnnotation(zhDesc = "时间区间 结束带等号")
    public static final String TIME_RANGE_WITH_END_EQ = "{} > {} AND {} <= {}";

    @GXFieldCommentAnnotation(zhDesc = "时间区间 开始结束不带等号")
    public static final String TIME_RANGE_WITHOUT_EQ = "{} > {} AND {} < {}";

    @GXFieldCommentAnnotation(zhDesc = "IN条件")
    public static final String IN = " IN ({})";

    @GXFieldCommentAnnotation(zhDesc = "NOT IN条件")
    public static final String NOT_IN = " NOT IN ({})";

    private GXBaseBuilderConstants() {
    }
}
