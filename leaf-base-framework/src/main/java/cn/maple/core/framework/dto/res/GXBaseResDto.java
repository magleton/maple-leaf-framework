package cn.maple.core.framework.dto.res;

import cn.maple.core.framework.dto.GXBaseDto;
import cn.maple.core.framework.util.GXSpringContextUtils;
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

    /**
     * 获取Spring中的bean对象
     *
     * @param clazz Bean的类型
     * @return Bean对象
     */
    public <R> R getBean(Class<R> clazz) {
        return GXSpringContextUtils.getBean(clazz);
    }

    /**
     * 获取Spring中的bean对象
     *
     * @param beanName     Bean的名字
     * @param requiredType Bean的类型
     * @return Bean对象
     */
    public <R> R getBean(String beanName, Class<R> requiredType) {
        return GXSpringContextUtils.getBean(beanName, requiredType);
    }
}
