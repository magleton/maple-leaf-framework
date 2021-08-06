package com.geoxus.core.common.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 用于和PHP通信时的加解密，加解密双方的数据
 * 此类不可随便更改，否则可能导致双方加解密不成功
 *
 * @author zj chen <britton@126.com>
 */
public class GXAuthCodeUtils {
    /**
     * private static MD5 md5 = new MD5();
     * private static BASE64 base64 = new BASE64();
     * 从字符串的指定位置截取指定长度的子字符串
     *
     * @param str        原字符串
     * @param startIndex 子字符串的起始位置
     * @param length     子字符串的长度
     * @return 子字符串
     */
    private static String cutString(String str, int startIndex, int length) {
        if (startIndex >= 0) {
            if (length < 0) {
                length = length * -1;
                if (startIndex - length < 0) {
                    length = startIndex;
                    startIndex = 0;
                } else {
                    startIndex = startIndex - length;
                }
            }
            if (startIndex > str.length()) {
                return "";
            }
        } else {
            if (length < 0) {
                return "";
            }
            if (length + startIndex <= 0) {
                return "";
            }
            length = length + startIndex;
            startIndex = 0;
        }
        if (str.length() - startIndex < length) {
            length = str.length() - startIndex;
        }
        return str.substring(startIndex, startIndex + length);
    }

    /**
     * 从字符串的指定位置开始截取到字符串结尾的了符串
     *
     * @param str        原字符串
     * @param startIndex 子字符串的起始位置
     * @return 子字符串
     */
    private static String cutString(String str, int startIndex) {
        return cutString(str, startIndex, str.length());
    }

    /**
     * 返回文件是否存在
     *
     * @param filename 文件名
     * @return boolean
     */
    public static boolean fileExists(String filename) {
        File f = new File(filename);
        return f.exists();
    }

    /**
     * MD5函数
     *
     * @param str 原始字符串
     * @return MD5结果
     */
    private static String md5(String str) {
        return SecureUtil.md5(str);
    }

    /**
     * 字段串是否为Null或为""(空)
     *
     * @param str 字符串参数
     * @return boolean
     */
    public static boolean strIsNullOrEmpty(String str) {
        return str == null || str.trim().equals("");
    }

    /**
     * 用于 RC4 处理密码
     *
     * @param pass byte[]参数
     * @param kLen 密钥长度，一般为256
     * @return byte[]
     */
    private static byte[] getKey(byte[] pass, int kLen) {
        byte[] mBox = new byte[kLen];
        for (int i = 0; i < kLen; i++) {
            mBox[i] = (byte) i;
        }
        int j = 0;
        for (int i = 0; i < kLen; i++) {
            j = (j + ((mBox[i] + 256) % 256) + pass[i % pass.length]) % kLen;
            byte temp = mBox[i];
            mBox[i] = mBox[j];
            mBox[j] = temp;
        }
        return mBox;
    }

    /**
     * 生成随机字符
     *
     * @param lens 随机字符长度
     * @return 随机字符
     */
    private static String randomString(int lens) {
        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int cLens = chars.length;
        StringBuilder sCode = new StringBuilder();
        for (int i = 0; i < lens; i++) {
            sCode.append(chars[RandomUtil.randomInt(cLens)]);
        }
        return sCode.toString();
    }

    /**
     * 使用 DisCuz authCode 方法对字符串加密
     *
     * @param source 原始字符串
     * @param key    密钥
     * @param expiry 加密字串有效时间，单位是秒
     * @return 加密结果
     */
    public static String authCodeEncode(String source, String key, int expiry) {
        return authCode(source, key, GXAuthCodeMode.ENCODE, expiry);
    }

    /**
     * 使用 DisCuz authCode 方法对字符串加密
     *
     * @param source 原始字符串
     * @param key    密钥
     * @return 加密结果
     */
    public static String authCodeEncode(String source, String key) {
        return authCode(source, key, GXAuthCodeMode.ENCODE, 0);
    }

    /**
     * 使用 DisCuz authCode 方法对字符串解密
     *
     * @param source 原始字符串
     * @param key    密钥
     * @return 解密结果
     */
    public static String authCodeDecode(String source, String key) {
        return authCode(source, key, GXAuthCodeMode.DECODE, 0);
    }

    /**
     * 使用 变形的 rc4 编码方法对字符串进行加密或者解密
     *
     * @param source    原始字符串
     * @param key       密钥
     * @param operation 操作 加密还是解密
     * @param expiry    加密字串过期时间
     * @return 加密或者解密后的字符串
     */
    private static String authCode(String source, String key, GXAuthCodeMode operation, int expiry) {
        try {
            if (source == null || key == null) {
                return "{}";
            }
            int cKeyLength = 4;
            String keyA;
            String keyB;
            String keyC;
            String cryptKey;
            String result;
            key = md5(key);
            keyA = md5(cutString(key, 0, 16));
            keyB = md5(cutString(key, 16, 16));
            keyC = operation == GXAuthCodeMode.DECODE ? cutString(source, 0, cKeyLength) : randomString(cKeyLength);
            cryptKey = keyA + md5(keyA + keyC);
            if (operation == GXAuthCodeMode.DECODE) {
                byte[] temp;
                temp = Base64.decode(cutString(source, cKeyLength).getBytes(StandardCharsets.UTF_8));
                result = new String(rc4(temp, cryptKey));
                if ((result.indexOf("0000000000") == 0
                        || Integer.parseInt(cutString(result, 0, 10)) - DateUtil.currentSeconds() > 0)
                        && cutString(result, 10, 16).equals(
                        cutString(md5(cutString(result, 26) + keyB), 0, 16))) {
                    return cutString(result, 26);
                }
                return "{}";
            } else {
                expiry = expiry > 0 ? (int) (DateUtil.currentSeconds() + expiry) : 0;
                source = String.format("%010d", expiry) + cutString(md5(source + keyB), 0, 16) + source;
                byte[] temp = rc4(source.getBytes(StandardCharsets.UTF_8), cryptKey);
                return String.format("%s%s", keyC, Base64.encode(temp));
            }
        } catch (Exception e) {
            return "{}";
        }
    }

    /**
     * RC4 原始算法
     *
     * @param input 原始字串数组
     * @param pass  密钥
     * @return 处理后的字串数组
     */
    private static byte[] rc4(byte[] input, String pass) {
        if (input == null || pass == null) {
            return new byte[0];
        }
        byte[] output = new byte[input.length];
        byte[] mBox = getKey(pass.getBytes(), 256);
        // 加密
        int i = 0;
        int j = 0;
        for (int offset = 0; offset < input.length; offset++) {
            i = (i + 1) % mBox.length;
            j = (j + ((mBox[i] + 256) % 256)) % mBox.length;
            byte temp = mBox[i];
            mBox[i] = mBox[j];
            mBox[j] = temp;
            byte a = input[offset];
            // byte b = mBox[(mBox[i] + mBox[j] % mBox.Length) % mBox.Length];
            // mBox[j] 一定比 mBox.Length 小，不需要在取模
            byte b = mBox[(toInt(mBox[i]) + toInt(mBox[j])) % mBox.length];
            output[offset] = (byte) ((int) a ^ toInt(b));
        }
        return output;
    }

    /**
     * 将字节转换为数字
     *
     * @param b byte参数
     * @return int
     */
    private static int toInt(byte b) {
        return (b + 256) % 256;
    }

    /**
     * 操作类型
     */
    private enum GXAuthCodeMode {
        ENCODE, DECODE
    }
}
