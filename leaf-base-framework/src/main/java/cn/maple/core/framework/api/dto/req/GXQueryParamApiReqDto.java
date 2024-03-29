package cn.maple.core.framework.api.dto.req;

import cn.maple.core.framework.constant.GXCommonConstant;
import cn.maple.core.framework.dto.inner.condition.GXCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("all")
public class GXQueryParamApiReqDto extends GXBaseApiReqDto {
    private static final long serialVersionUID = -1375221810158532151L;

    /**
     * 当前页
     */
    private Integer page = GXCommonConstant.DEFAULT_CURRENT_PAGE;

    /**
     * 每页大小
     */
    private Integer pageSize = GXCommonConstant.DEFAULT_PAGE_SIZE;

    /**
     * DB查询条件
     */
    private List<GXCondition<?>> condition;

    /**
     * 排序字段
     * eg:
     * <code>
     * Map&lt;String ,String&gt; orderByField = new HashMap<>();
     * orderByField.put("created_at" , "desc");
     * orderByField.put("username" , "asc");
     * </code>
     */
    private Map<String, String> orderByField;

    /**
     * 分组字段
     */
    private Set<String> groupByField;

    /**
     * 额外参数(用于回调)
     */
    private Object extraData;
}
