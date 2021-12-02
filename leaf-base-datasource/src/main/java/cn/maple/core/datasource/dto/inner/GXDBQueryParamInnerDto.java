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
     * 需要查询的主表名字 , 在有join查询时
     */
    private String tableName;

    /**
     * 需要查询的主表名字的别名 , 在有join查询时
     */
    private String tableNameAlias;

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
     * 需要查询的数据列
     */
    private Set<String> columns;

    /**
     * 排序字段
     * eg:
     * Dict.create().set("created_at" , "desc").set("username" , "asc);
     */
    private Dict orderByField;

    /**
     * 分组字段
     */
    private Set<String> groupByField;

    /**
     * JOIN链接信息
     */
    private transient Table<String, String, Object> joins;
}
