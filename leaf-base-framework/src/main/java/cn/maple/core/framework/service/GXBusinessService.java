package cn.maple.core.framework.service;

import cn.maple.core.framework.event.GXBaseEvent;

public interface GXBusinessService {
    /**
     * 加密手机号码
     *
     * @param phoneNumber 明文手机号
     * @return String
     */
    String encryptedPhoneNumber(String phoneNumber);

    /**
     * 解密手机号码
     *
     * @param encryptPhoneNumber 加密手机号
     * @return String
     */
    String decryptedPhoneNumber(String encryptPhoneNumber);

    /**
     * 隐藏手机号码的指定几位为指定的字符
     *
     * @param phoneNumber  手机号码
     * @param startInclude 开始字符位置(包含,从0开始)
     * @param endExclude   结束字符位置
     * @param replacedChar 替换为的字符
     * @return String
     */
    String hiddenPhoneNumber(CharSequence phoneNumber, int startInclude, int endExclude, char replacedChar);

    /**
     * 派发事件 (异步事件可以通过在监听器上面添加@Async注解实现)
     *
     * @param event ApplicationEvent对象
     */
    <R> void publishEvent(GXBaseEvent<R> event);

    /**
     * 获取实体中指定指定的值
     * <pre>
     *     {@code
     *     getSingleJSONFieldValueByEntity(
     *       GoodsEntity,
     *       "ext.name",
     *       Integer.class
     *       )
     *     }
     * </pre>
     *
     * @param entity 实体对象
     * @param path   路径
     * @return R
     */
    <T, R> R getSingleFieldValueByEntity(T entity, String path, Class<R> type);

    /**
     * 获取实体中指定指定的值
     * <pre>
     *     {@code
     *     getSingleJSONFieldValueByEntity(
     *       GoodsEntity,
     *       "ext.name",
     *       Integer.class
     *       )
     *     }
     * </pre>
     *
     * @param entity       实体对象
     * @param path         路径
     * @param defaultValue 默认值
     * @return R
     */
    <T, R> R getSingleFieldValueByEntity(T entity, String path, Class<R> type, R defaultValue);
}
