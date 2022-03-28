package cn.maple.core.framework.dto.res;

import cn.maple.core.framework.dto.GXBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXBaseResDto extends GXBaseDto {
    /**
     * 需要获取关联数据的字段名字
     * 在不同场景下可以获取不同的关联字段的数据
     * eg:
     * <p>
     * 在获取SKU的数据时
     * 1、可以单独的获取品牌信息、商品信息、资源信息等
     * 2、也可以同时获取所有的关联信息
     * 3、也可以不获取任何的关联信息
     */
    protected transient List<String> gainAssociatedFields;
}
