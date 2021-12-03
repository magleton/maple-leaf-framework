package cn.maple.core.framework.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录用户验证拦截器
 *
 * @author britton
 */
@Slf4j
@SuppressWarnings("all")
public abstract class GXAuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("请自定义实现GXAuthorizationInterceptor类");
        // TODO 如果需要支持同一时间只能有一个客户端生效 可以在token中设置一个标识(eg : 时间戳、md5当前的Token...)
        // TODO 然后使用这个Token中的标识和存储器(缓存、数据库)中存储的相应Token中的标识对比即可
        return true;
    }
}
