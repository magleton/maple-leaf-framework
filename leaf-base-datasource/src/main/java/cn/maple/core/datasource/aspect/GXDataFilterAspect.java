package cn.maple.core.datasource.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.datasource.annotation.GXDataFilter;
import cn.maple.core.datasource.dto.GXDataFilterInnerDto;
import cn.maple.core.datasource.service.GXDataScopeService;
import cn.maple.core.datasource.util.GXThreadLocalUtils;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXSpringContextUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
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

    @Before("dataFilterPointCut()")
    public void dataFilter(JoinPoint point) {
        Object params = point.getArgs()[0];
        if (Objects.nonNull(params) && (params instanceof Dict)) {
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
                GXThreadLocalUtils.getDataFilterThreadLocal().set(dataScope);
            } catch (Exception e) {
                throw new GXBusinessException(e.getMessage(), e);
            }
            return;
        }

        throw new GXBusinessException("数据权限错误", 10013);
    }

    /**
     * 获取数据过滤的SQL
     */
    private String getSqlFilter(JoinPoint point) throws Exception {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = point.getTarget().getClass().getDeclaredMethod(signature.getName(), signature.getParameterTypes());
        GXDataFilter dataFilter = method.getAnnotation(GXDataFilter.class);

        // 获取表的别名
        String tableAlias = dataFilter.tableAlias();
        if (StringUtils.isNotBlank(tableAlias)) {
            tableAlias += ".";
        }

        StringBuilder sqlFilter = new StringBuilder();
        sqlFilter.append(" (");
        GXDataScopeService dataScopeService = GXSpringContextUtils.getBean(GXDataScopeService.class);
        // 部门ID列表
        List<Long> deptIdList = Objects.nonNull(dataScopeService) ? dataScopeService.getDeptIdLst() : Collections.emptyList();
        if (CollUtil.isNotEmpty(deptIdList)) {
            sqlFilter.append(tableAlias).append(dataFilter.deptId());
            sqlFilter.append(" in(").append(CharSequenceUtil.join(",", deptIdList)).append(")");
        }

        // 查询本人数据
        if (CollUtil.isNotEmpty(deptIdList)) {
            sqlFilter.append(" or ");
        }
        Long userId = Objects.nonNull(dataScopeService) ? dataScopeService.getLoginUserId() : 0L;
        sqlFilter.append(tableAlias).append(dataFilter.userId()).append("=").append(userId);

        sqlFilter.append(")");

        return sqlFilter.toString();
    }
}