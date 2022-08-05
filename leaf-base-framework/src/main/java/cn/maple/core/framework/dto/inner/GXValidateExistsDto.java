package cn.maple.core.framework.dto.inner;

import cn.maple.core.framework.dto.GXBaseDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class GXValidateExistsDto extends GXBaseDto {
    /**
     * 字段名字
     */
    private String fieldName;

    /**
     * 表名字
     */
    private String tableName;

    /**
     * 需要验证的值
     */
    @SuppressWarnings("all")
    private Object value;

    /**
     * 附加条件 SpEL表达式
     * 用于计算结果是否满足预期
     */
    private String spEL;

    /**
     * 附加验证条件 用于指定一些固定条件值
     */
    private String condition;
}
