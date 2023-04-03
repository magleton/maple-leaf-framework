package cn.maple.sso.service;

import cn.maple.sso.annotation.GXLoginAnnotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface GXAuthorizationInterceptorService {
    /**
     * 自己定义拦截规则
     * <p>
     * 返回 true 放行
     * 返回 false 继续走后面的逻辑
     *
     * @param request    请求对象
     * @param response   响应对象
     * @param annotation 自定义注解
     * @return true / false
     */
    boolean interceptor(HttpServletRequest request, HttpServletResponse response, GXLoginAnnotation annotation);
}
