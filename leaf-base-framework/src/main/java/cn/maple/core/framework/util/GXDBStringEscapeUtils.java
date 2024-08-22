package cn.maple.core.framework.util;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * GXDBStringEscapeUtils ，数据库字符串转义
 *
 * @author 塵子曦
 */
public class GXDBStringEscapeUtils {
    private static final Pattern SQL_SYNTAX_PATTERN = Pattern.compile("(insert|delete|update|select|create|drop|truncate|grant|alter|deny|revoke|call|execute|exec|declare|show|rename|set)\\s+.*(into|from|set|where|table|database|view|index|on|cursor|procedure|trigger|for|password|union|and|or)|(select\\s*\\*\\s*from\\s+)|(and|or)\\s+.*", 2);
    private static final Pattern SQL_COMMENT_PATTERN = Pattern.compile("'.*(or|union|--|#|/\\*|;)", 2);

    /**
     * 字符串是否需要转义
     *
     * @param str ignore
     * @param len ignore
     * @return 是否需要转义
     */
    private static boolean isEscapeNeededForString(String str, int len) {
        boolean needsHexEscape = false;
        for (int i = 0; i < len; ++i) {
            char c = str.charAt(i);
            switch (c) {
                /* Must be escaped for 'mysql' */
                case 0:
                    needsHexEscape = true;
                    break;
                /* Must be escaped for logs */
                case '\n':
                    needsHexEscape = true;
                    break;
                case '\r':
                    needsHexEscape = true;
                    break;
                case '\\':
                    needsHexEscape = true;
                    break;
                case '\'':
                    needsHexEscape = true;
                    break;
                /* Better safe than sorry */
                case '"':
                    needsHexEscape = true;
                    break;
                /* This gives problems on Win32 */
                case '\032':
                    needsHexEscape = true;
                    break;
                default:
                    break;
            }
            if (needsHexEscape) {
                // no need to scan more
                break;
            }
        }
        return needsHexEscape;
    }

    /**
     * 转义字符串。纯转义，不添加单引号。
     *
     * @param escapeStr 被转义的字符串
     * @return 转义后的字符串
     */
    public static String escapeRawString(String escapeStr) {
        int stringLength = escapeStr.length();
        if (isEscapeNeededForString(escapeStr, stringLength)) {
            StringBuilder buf = new StringBuilder((int) (escapeStr.length() * 1.1));
            //
            // Note: buf.append(char) is _faster_ than appending in blocks,
            // because the block append requires a System.arraycopy().... go
            // figure...
            //
            for (int i = 0; i < stringLength; ++i) {
                char c = escapeStr.charAt(i);
                switch (c) {
                    /* Must be escaped for 'mysql' */
                    case 0:
                        buf.append('\\');
                        buf.append('0');

                        break;
                    /* Must be escaped for logs */
                    case '\n':
                        buf.append('\\');
                        buf.append('n');

                        break;

                    case '\r':
                        buf.append('\\');
                        buf.append('r');

                        break;

                    case '\\':
                        buf.append('\\');
                        buf.append('\\');

                        break;

                    case '\'':
                        buf.append('\\');
                        buf.append('\'');

                        break;
                    /* Better safe than sorry */
                    case '"':
                        buf.append('\\');
                        buf.append('"');

                        break;
                    /* This gives problems on Win32 */
                    case '\032':
                        buf.append('\\');
                        buf.append('Z');
                        break;
                    default:
                        buf.append(c);
                }
            }
            return buf.toString();
        } else {
            return escapeStr;
        }
    }

    /**
     * 转义字符串
     *
     * @param escapeStr 被转义的字符串
     * @return 转义后的字符串
     */
    public static String escapeString(String escapeStr) {
        if (escapeStr.matches("'(.+)'")) {
            escapeStr = escapeStr.substring(1, escapeStr.length() - 1);
        }
        return "'" + escapeRawString(escapeStr) + "'";
    }

    public static boolean check(String value) {
        Objects.requireNonNull(value);
        return SQL_COMMENT_PATTERN.matcher(value).find() || SQL_SYNTAX_PATTERN.matcher(value).find();
    }

    public static String removeEscapeCharacter(String text) {
        Objects.nonNull(text);
        return text.replaceAll("\"", "").replaceAll("'", "");
    }
}