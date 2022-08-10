package cn.maple.core.datasource.aspect;

import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.datasource.annotation.GXDataFilter;
import cn.maple.core.datasource.dto.GXDataFilterInnerDto;
import cn.maple.core.datasource.service.GXDataScopeService;
import cn.maple.core.datasource.util.GXDataFilterThreadLocalUtils;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXSpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 数据过滤，切面处理类
 *
 * @author 塵渊 britton@126.com
 */
@Aspect
@Component
@Slf4j
public class GXDataFilterAspect {
    @Pointcut("@annotation(cn.maple.core.datasource.annotation.GXDataFilter)")
    public void dataFilterPointCut() {

    }

    @After("dataFilterPointCut()")
    @AfterThrowing("dataFilterPointCut()")
    public void dataFilterAfter() {
        GXDataFilterInnerDto dataFilterInnerDto = GXDataFilterThreadLocalUtils.getDataFilterInnerDto();
        if (Objects.nonNull(dataFilterInnerDto) && CharSequenceUtil.isNotEmpty(dataFilterInnerDto.getSqlFilter())) {
            // 执行完成，要清除当前权限Sql
            GXDataFilterThreadLocalUtils.cleanDataFilterInnerDto();
        }
    }

    @Before("dataFilterPointCut()")
    public void dataFilterBefore(JoinPoint point) {
        GXDataScopeService dataScopeService = GXSpringContextUtils.getBean(GXDataScopeService.class);
        boolean isSuperAdmin = !Objects.nonNull(dataScopeService) || dataScopeService.isSuperAdmin();
        // 如果是超级管理员，则不进行数据过滤
        if (isSuperAdmin) {
            return;
        }

        // 否则进行数据过滤
        try {
            String sqlFilter = getSqlFilter(point);
            GXDataFilterInnerDto dataScope = new GXDataFilterInnerDto(sqlFilter);
            GXDataFilterThreadLocalUtils.setDataFilterInnerDto(dataScope);
        } catch (Exception e) {
            throw new GXBusinessException(e.getMessage(), e);
        }
    }

    /**
     * 获取数据过滤的SQL
     */
    private String getSqlFilter(JoinPoint point) throws Exception {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = point.getTarget().getClass().getDeclaredMethod(signature.getName(), signature.getParameterTypes());
        GXDataFilter dataFilter = method.getAnnotation(GXDataFilter.class);
        GXDataScopeService dataScopeService = GXSpringContextUtils.getBean(GXDataScopeService.class);
        assert dataScopeService != null;
        return dataScopeService.getSqlFilter(dataFilter);
    }
}