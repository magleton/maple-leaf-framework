package cn.maple.core.datasource.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.maple.core.datasource.annotation.GXDataFilter;
import cn.maple.core.framework.util.GXSpringContextUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
    default String getSqlFilter(GXDataFilter dataFilter) {
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
}
