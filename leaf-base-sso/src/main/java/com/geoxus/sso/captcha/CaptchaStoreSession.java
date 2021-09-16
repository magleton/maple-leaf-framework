package com.geoxus.sso.captcha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * 图片验证码内容存储 Session 设置 1 分钟过期
 * </p>
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
public class CaptchaStoreSession implements ICaptchaStore {

  private HttpSession httpSession;

  private CaptchaStoreSession() {
    // to do nothing
  }

  public CaptchaStoreSession(HttpServletRequest request) {
    this.httpSession = request.getSession();
  }

  @Override
  public String get(String ticket) {
    return String.valueOf(httpSession.getAttribute(ticket));
  }

  @Override
  public boolean put(String ticket, String captcha) {
    httpSession.setMaxInactiveInterval(60);
    httpSession.setAttribute(ticket, captcha);
    return true;
  }
}
