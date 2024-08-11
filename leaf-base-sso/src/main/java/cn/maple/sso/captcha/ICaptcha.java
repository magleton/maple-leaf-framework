package cn.maple.sso.captcha;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * <p>
 * 图片验证码接口
 * </p>
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
public interface ICaptcha extends Serializable {
    /**
     * <p>
     * 生成图片验证码
     * </p>
     *
     * @param request 当前请求
     * @param out     输出流
     * @param ticket  验证码票据
     * @return String 验证码内容
     */
    void generate(HttpServletRequest request, OutputStream out, String ticket) throws IOException;

    /**
     * <p>
     * 判断验证码是否正确
     * </p>
     *
     * @param request 当前请求
     * @param ticket  验证码票据
     * @param captcha 验证码内容
     * @return boolean true 正确，false 错误
     */
    boolean verification(HttpServletRequest request, String ticket, String captcha);

}
