package cn.maple.core.framework.dto.inner;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.dto.GXBaseDto;
import com.google.common.collect.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
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
     * Dict.create().set("created_at" , "desc").set("username" , "asc");
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
     * 额外参数
     * 配合methodName一起使用
     */
    private Dict customerData;

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
     * 限制条数
     */
    private Integer limit;

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

    /**
     * 需要获取关联数据的字段名字
     * 在不同场景下可以获取不同的关联字段的数据
     * eg:
     * 在获取SKU的数据时
     * 1、可以单独的获取品牌信息、商品信息、资源信息等
     * 2、也可以同时获取所有的关联信息
     * 3、也可以不获取任何的关联信息
     */
    private List<String> gainAssociatedFields;

    /**
     * 额外参数
     * 用于一些特殊场景
     * eg:
     * 1、不合适放入condition中的参数
     * 2、需要使用该参数调整查询的规则
     */
    private transient Dict params;
}
