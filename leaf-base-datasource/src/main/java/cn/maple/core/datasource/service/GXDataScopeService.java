package cn.maple.core.datasource.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.datasource.annotation.GXDataFilter;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.inner.condition.GXCondition;
import cn.maple.core.framework.dto.inner.condition.GXIgnoreDataFilterCondition;
import cn.maple.core.framework.util.GXSpringContextUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.aspectj.lang.JoinPoint;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface GXDataScopeService {
    /**
     * 获取当前登录人的所属部门列表
     *
     * @return 部门列表
     */
    default List<Long> getDeptIdLst() {
        return Collections.emptyList();
    }

    /**
     * 获取当前登录人的ID
     *
     * @return 登陆人ID
     */
    default Long getLoginUserId() {
        return 0L;
    }

    /**
     * 判断当前登陆人是否是超级用户
     * 超级用户不需要数据过滤
     *
     * @return boolean
     */
    default boolean isSuperAdmin() {
        return false;
    }

    /**
     * 组合SQL Filter语句
     *
     * @param dataFilter 数据过滤
     * @return SQL过滤语句
     */
    default String getSqlFilter(GXDataFilter dataFilter, JoinPoint point) {
        boolean hasIgnoreDataFilterCondition = checkIgnoreDataFilter(point);
        if (hasIgnoreDataFilterCondition) {
            return "";
        }
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
            sqlFilter.append(tableAlias).append(dataFilter.deptIdFieldName());
            sqlFilter.append(" in(").append(CharSequenceUtil.join(",", deptIdList)).append(")");
        }

        // 查询本人数据
        if (CollUtil.isNotEmpty(deptIdList)) {
            sqlFilter.append(" or ");
        }
        Long userId = Objects.nonNull(dataScopeService) ? dataScopeService.getLoginUserId() : 0L;
        sqlFilter.append(tableAlias).append(dataFilter.userIdFieldName()).append("=").append(userId);

        sqlFilter.append(")");
        return sqlFilter.toString();
    }

    /**
     * 过滤出有GXBaseQueryParamInnerDto参数的查询
     * 处理参数中是否带有GXIgnoreDataFilterCondition条件,如果带有该条件,这不需要数据权限过滤,并且需要将其从查询条件移除
     * 如果加上了该条件 则不会有数据权限过滤的功能
     *
     * @param point 切点
     * @return boolean 是否有不需要数据过滤的条件
     */
    private boolean checkIgnoreDataFilter(JoinPoint point) {
        List<Object> args = Arrays.asList(point.getArgs());
        if (CollUtil.isEmpty(List.of(args))) {
            return true;
        }
        List<Object> argsLst = args.stream().filter(t -> t.getClass().isAssignableFrom(GXBaseQueryParamInnerDto.class)).collect(Collectors.toList());
        if (CollUtil.isEmpty(argsLst)) {
            return true;
        }
        GXBaseQueryParamInnerDto queryParams = (GXBaseQueryParamInnerDto) argsLst.get(0);
        // 如果手动设置了true 就直接返回
        if (queryParams.isIgnoreDataFilter()) {
            return true;
        }
        // 处理条件中含有GXIgnoreDataFilterCondition类的条件
        List<Integer> removeIndexLst = CollUtil.newArrayList();
        List<GXCondition<?>> conditionLst = queryParams.getCondition();
        if (CollUtil.isNotEmpty(conditionLst)) {
            for (int i = 0, len = conditionLst.size(); i < len; i++) {
                if (conditionLst.get(i).getClass().isAssignableFrom(GXIgnoreDataFilterCondition.class)) {
                    queryParams.getCondition().remove(i);
                    removeIndexLst.add(i);
                }
            }
            return CollUtil.isNotEmpty(removeIndexLst);
        }
        return false;
    }
}
