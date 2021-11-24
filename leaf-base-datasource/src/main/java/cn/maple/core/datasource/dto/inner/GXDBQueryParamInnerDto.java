package cn.maple.core.datasource.dto.inner;

import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.dto.GXBaseDto;
import com.google.common.collect.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class GXDBQueryParamInnerDto extends GXBaseDto {
    /**
     * 需要查询的表名字
     */
    private String tableName;

    /**
     * 当前页
     */
    private Integer page;

    /**
     * 每页数据条数
     */
    private Integer pageSize;

    /**
     * 搜索条件
     */
    private transient Table<String, String, Object> condition;

    /**
     * 需要返回的数据列
     */
    private Set<String> columns;

    /**
     * 排序字段
     * Dict.create().set("created_at" , "desc").set("username" , "asc);
     */
    private Dict orderByField;

    /**
     * 分组字段
     */
    private Set<String> groupByField;
}
