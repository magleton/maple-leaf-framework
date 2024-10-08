package cn.maple.sso.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface GXAuthorizationInterceptorService {
    /**
     * 自己定义拦截规则
     * <p>
     * 返回 true 放行
     * 返回 false 继续走后面的逻辑
     * 如果不满足业务条件  直接抛出异常即可
     *
     * @param request    请求对象
     * @param response   响应对象
     * @return true / false
     */
    boolean interceptor(HttpServletRequest request, HttpServletResponse response);
}
