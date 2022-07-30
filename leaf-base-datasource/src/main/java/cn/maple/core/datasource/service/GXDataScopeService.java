package cn.maple.core.datasource.service;

import java.util.Collections;
import java.util.List;

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
     * @return
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
}
