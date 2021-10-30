package cn.maple.sso.captcha;

/**
 * <p>
 * 图片验证码票据存储接口
 * </p>
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
public interface ICaptchaStore {
    /**
     * <p>
     * 获取验证码票据的验证内容
     * </p>
     *
     * @param ticket 验证码票据
     * @return String 验证码内容
     */
    String get(String ticket);

    /**
     * <p>
     * 设置验证码票据的验证内容
     * </p>
     *
     * @param ticket  验证码票据
     * @param captcha 验证码内容
     * @return boolean true 正确，false 错误
     */
    boolean put(String ticket, String captcha);

}
