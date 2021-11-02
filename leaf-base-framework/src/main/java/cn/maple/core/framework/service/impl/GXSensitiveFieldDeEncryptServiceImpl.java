package cn.maple.core.framework.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.core.framework.service.GXSensitiveFieldDeEncryptService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

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
            return GXCommonUtils.getAES(key).encryptBase64(dataStr);
        }
        return GXCommonUtils.getAES().encryptBase64(dataStr);
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
            return GXCommonUtils.getAES(key).decryptStr(dataStr);
        }
        return GXCommonUtils.getAES().decryptStr(dataStr);
    }
}
