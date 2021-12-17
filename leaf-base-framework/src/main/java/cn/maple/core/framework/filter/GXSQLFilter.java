package cn.maple.core.framework.filter;

import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.exception.GXBusinessException;

/**
 * SQL过滤
 */
public class GXSQLFilter {

    /**
     * SQL注入过滤
     *
     * @param str 待验证的字符串
     */
    public static String sqlInject(String str) {
        if (CharSequenceUtil.isBlank(str)) {
            return null;
        }
        //去掉'|"|;|\字符
        str = CharSequenceUtil.replace(str, "'", "");
        str = CharSequenceUtil.replace(str, "\"", "");
        str = CharSequenceUtil.replace(str, ";", "");
        str = CharSequenceUtil.replace(str, "\\", "");

        //转换成小写
        str = str.toLowerCase();

        //非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alert", "drop"};

        //判断是否包含非法字符
        for (String keyword : keywords) {
            if (str.contains(keyword)) {
                throw new GXBusinessException("包含非法字符");
            }
        }
        return str;
    }
}
