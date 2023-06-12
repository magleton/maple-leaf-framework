package cn.maple.core.framework.web.interceptor;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.constant.GXCommonConstant;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * 验证部署环境是否一致
 *
 * @author 塵子曦
 */
@Component
public class GXVerifyDeployEnvironmentInterceptor extends GXAuthorizationInterceptor {
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws IOException {
        return verifyDeployEnvironmentConsistency(request, response);
    }

    private boolean verifyDeployEnvironmentConsistency(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestHeaderValue = request.getHeader(GXCommonConstant.DEPLOY_REQUEST_ENV_HEADER_NAME);
        String proxyHeaderValue = System.getProperty(GXCommonConstant.DEPLOY_ENV_HEADER_NAME);
        if (!CharSequenceUtil.equals(requestHeaderValue, proxyHeaderValue)) {
            Dict data = Dict.create().set("code", HttpStatus.HTTP_FORBIDDEN).set("msg", "请求的部署环境不一致!").set("data", null);
            JSONConfig jsonConfig = new JSONConfig();
            jsonConfig.setIgnoreNullValue(false);
            response.setContentType("application/json;charset=UTF-8");
            response.addIntHeader("Allow", HttpStatus.HTTP_FORBIDDEN);
            response.getWriter().write(JSONUtil.toJsonStr(data, jsonConfig));
            return false;
        }
        return true;
    }
}
