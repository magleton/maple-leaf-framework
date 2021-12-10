package cn.maple.core.framework.dto.inner;

import cn.hutool.core.bean.copier.CopyOptions;
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
public class GXBaseQueryParamInnerDto extends GXBaseDto {
    /**
     * 需要查询的主表名字(在有join查询时,需要有主表、次表的区分)
     */
    private String tableName;

    /**
     * 需要查询的主表名字的别名(在有join查询时,需要有主表、次表的区分)
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
     * 自定义mapper中的方法名字
     */
    private String mapperMethodName;

    /**
     * GXBaseData及其子类中的方法名字
     * 通常用于在需要额外处理一些逻辑时自动调用
     */
    private String methodName;

    /**
     * SQL中的having条件
     * <pre>
     * {@code
     * Set<String> having = CollUtil.newHashSet("SUM(area)>1000000" , "SUM(price) >= 1000");
     * }
     * </pre>
     */
    private Set<String> having;

    /**
     * 对象复制时的一些额外配置
     */
    private CopyOptions copyOptions;

    /**
     * 排除条件中的删除字段
     * 条件中不会出现删除的字段
     */
    private Boolean excludeDeletedFieldCondition;

    /**
     * JOIN链接信息
     * <pre>
     * {@code
     * Table<String, String, Table<String, String, Dict>> joins = HashBasedTable.create();
     * HashBasedTable<String, String, Dict> joinData = HashBasedTable.create();
     * joinData.put("子表别名" , "主表别名" , Dict.create().set("子表字段" , "主表字段"))
     * joins.put("链接类型" , "子表名字" , joinData);
     * }
     * </pre>
     */
    private transient Table<String, String, Table<String, String, Dict>> joins;
}
