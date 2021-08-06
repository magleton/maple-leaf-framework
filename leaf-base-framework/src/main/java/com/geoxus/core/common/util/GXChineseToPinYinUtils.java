package com.geoxus.core.common.util;

import cn.hutool.core.util.StrUtil;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GXChineseToPinYinUtils {
    private GXChineseToPinYinUtils() {
    }

    public static String getFirstLetter(String str) {
        try {
            String pinyin = PinyinHelper.getShortPinyin(str);
            final String sub = StrUtil.sub(pinyin, 0, 1);
            return sub.toUpperCase();
        } catch (PinyinException e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    public static String getFullFirstLetter(String str) {
        try {
            String pinyin = PinyinHelper.getShortPinyin(str);
            return pinyin.toUpperCase();
        } catch (PinyinException e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    public static String getFullLetter(String str) {
        try {
            final String pinyin = PinyinHelper.convertToPinyinString(str, " ");
            return pinyin.toUpperCase();
        } catch (PinyinException e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    public static String getFullLetter(String str, String separator, PinyinFormat pinyinFormat) {
        try {
            final String pinyin = PinyinHelper.convertToPinyinString(str, separator, pinyinFormat);
            return pinyin.toUpperCase();
        } catch (PinyinException e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }
}
