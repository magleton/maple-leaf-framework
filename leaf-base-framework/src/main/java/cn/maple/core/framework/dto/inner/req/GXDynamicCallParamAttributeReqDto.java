package cn.maple.core.framework.dto.inner.req;

import cn.maple.core.framework.dto.GXBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXDynamicCallParamAttributeReqDto extends GXBaseReqDto {
    /**
     * JAVA类型
     */
    private String javaType;

    /**
     * javaType的字段名字 , 用于组装出javaType对象
     */
    private String fieldName;

    /**
     * 数据来源
     * 1. token    来源于token 需要配合sourceFieldName来使用 JSONUtil.toBean(getHeader("token"),Dict.class).getStr(sourceFieldName)
     * 2. assign   来源于固定分配的值  需要配合fixedAssignedValue来使用
     * 3. callback 来源于动态调用自己的服务接口
     */
    private String dataSource;

    /**
     * 源字段名字 主要用于 {@code datasource.equals("token")} 的情况
     */
    private String sourceFieldName;

    /**
     * 固定值 主要用于 {@code datasource.equals("assign")} 的情况
     */
    private Object fixedAssignedValue;

    /**
     * 服务提供的服务类的名字 主要用于 {@code datasource.equals("callback")} 的情况
     * eg: cn.maple.cn.maple.core.framework.service.GXBaseService
     */
    private String callBackClassName;

    /**
     * 服务提供的服务的方法名字 主要用于 {@code datasource.equals("callback")} 的情况
     * 需要配合 callBackClassName一起使用
     * eg: cn.maple.cn.maple.core.framework.service.GXBaseService.getConstantsFields
     */
    private String callBackMethodName;
}
