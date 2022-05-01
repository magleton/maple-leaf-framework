package cn.maple.core.framework.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.maple.core.framework.service.GXSensitiveFieldDeEncryptService;
import cn.maple.core.framework.util.GXCommonUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@Order
public class GXSensitiveFieldDeEncryptServiceImpl implements GXSensitiveFieldDeEncryptService {
    /**
     * 加密数据
     *
     * @param dataStr 需要加密的数据
     * @param key     加密KEY
     * @param params  额外参数
     * @return 加密之后的数据(密文)
     */
    @Override
    public String encryptAlgorithm(String dataStr, String key, String... params) {
        if (CharSequenceUtil.isNotBlank(key)) {
            return getAES(key).encryptBase64(dataStr);
        }
        return getAES().encryptBase64(dataStr);
    }

    /**
     * 解密数据
     *
     * @param dataStr 需要加密的数据
     * @param key     解密key
     * @param params  额外参数
     * @return 解密之后的数据(明文)
     */
    @Override
    public String decryAlgorithm(String dataStr, String key, String... params) {
        if (CharSequenceUtil.isNotBlank(key)) {
            return getAES(key).decryptStr(dataStr);
        }
        return getAES().decryptStr(dataStr);
    }


    /**
     * 获取AES对象
     *
     * @param key AES的key
     * @return AES
     */
    private AES getAES(String key) {
        return SecureUtil.aes(key.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 获取AES对象
     *
     * @return AES
     */
    private AES getAES() {
        String key = GXCommonUtils.getEnvironmentValue("common.sensitive.data.key", String.class);
        if (CharSequenceUtil.isBlank(key)) {
            key = "XhFeV780D2218OBRm0xjcWvv";
        }
        return getAES(key);
    }
}
