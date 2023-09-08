package cn.maple.core.datasource.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.maple.core.datasource.annotation.GXDataFilter;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.inner.condition.GXCondition;
import cn.maple.core.framework.dto.inner.condition.GXConditionEQ;
import cn.maple.core.framework.dto.inner.condition.GXConditionIn;
import cn.maple.core.framework.dto.inner.condition.GXIgnoreDataFilterCondition;
import cn.maple.core.framework.util.GXSpringContextUtils;
import org.aspectj.lang.JoinPoint;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface GXDataScopeService {
    /**
     * 获取部门当前登录用户所属的部门ID列表
     *
     * @return List 部门ID列表
     */
    default Set<Number> getDeptIdLst() {
        return new HashSet<>();
    }

    /**
     * 获取当前登录人的部门筛选条件
     *
     * @param tableAlias       SQL语句中表名的别名
     * @param deptIdFieldNames SQL语句中表字段的名字
     * @return 部门的条件
     */
    default GXCondition<?> getDeptCondition(String tableAlias, String[] deptIdFieldNames) {
        Set<Number> deptIdLst = getDeptIdLst();
        if (CollUtil.isNotEmpty(deptIdLst)) {
            return new GXConditionIn(tableAlias, getDeptIdFieldName(deptIdFieldNames), deptIdLst);
        }
        return null;
    }

    /**
     * 获取当前登录人的用户筛选条件
     *
     * @param tableAlias       SQL语句中表名的别名
     * @param userIdFieldNames SQL语句中表字段的名字
     * @return 部门的条件
     */
    default GXCondition<?> getUserCondition(String tableAlias, String[] userIdFieldNames) {
        GXDataScopeService dataScopeService = GXSpringContextUtils.getBean(GXDataScopeService.class);
        if (ObjectUtil.isNull(dataScopeService)) {
            return null;
        }
        Long userId = dataScopeService.getLoginUserId();
        return new GXConditionEQ(tableAlias, getDeptIdFieldName(userIdFieldNames), userId);
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
        GXDataScopeService dataScopeService = GXSpringContextUtils.getBean(GXDataScopeService.class);
        if (hasIgnoreDataFilterCondition || ObjectUtil.isNull(dataScopeService)) {
            return "";
        }
        // 获取表的别名
        String tableAlias = dataFilter.tableAlias();

        // 表字段的名字
        String[] deptIdFieldNames = dataFilter.deptIdFieldNames();
        String[] userIdFieldNames = dataFilter.userIdFieldNames();

        // 构建sqlFilter对象
        StringBuilder sqlFilter = new StringBuilder();
        sqlFilter.append(" (");

        // 查询本人数据
        GXCondition<?> userIdCondition = getUserCondition(tableAlias, userIdFieldNames);
        sqlFilter.append(userIdCondition.whereString());

        // 部门ID列表
        GXCondition<?> deptCondition = dataScopeService.getDeptCondition(tableAlias, deptIdFieldNames);
        if (ObjectUtil.isNotNull(deptCondition)) {
            String s = deptCondition.whereString();
            // 添加或条件
            sqlFilter.append(" or ").append(s);
        }
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

    /**
     * 获取"标识"部门字段的数据库表的名字 eg dept_id
     *
     * @param deptIdFieldNames 注解上面标识的数据库字段名字
     */
    default String getDeptIdFieldName(String[] deptIdFieldNames) {
        return deptIdFieldNames[0];
    }

    /**
     * 获取"标识"用户字段的数据库表的名字 eg user_id
     *
     * @param userIdFieldNames 注解上面标识的数据库字段名字
     */
    default String getUserIdFieldName(String[] userIdFieldNames) {
        return userIdFieldNames[0];
    }
}
