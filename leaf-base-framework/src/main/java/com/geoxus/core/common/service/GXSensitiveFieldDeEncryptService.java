package com.geoxus.core.common.service;

/**
 * 对指定敏感数据的加解密服务实现
 */
public interface GXSensitiveFieldDeEncryptService {
    /**
     * 加密数据
     *
     * @param dataStr 需要加密的数据
     * @param key     加密KEY
     * @param params  额外参数
     * @return 加密之后的数据(密文)
     */
    String encryptAlgorithm(String dataStr, String key, String... params);

    /**
     * 解密数据
     *
     * @param dataStr 需要加密的数据
     * @param key     解密key
     * @param params  额外参数
     * @return 解密之后的数据(明文)
     */
    String decryAlgorithm(String dataStr, String key, String... params);
}
