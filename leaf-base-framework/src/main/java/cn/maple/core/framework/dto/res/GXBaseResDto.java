package cn.maple.core.framework.dto.res;

import cn.maple.core.framework.dto.GXBaseDto;
import cn.maple.core.framework.util.GXSpringContextUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXBaseResDto extends GXBaseDto {
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
