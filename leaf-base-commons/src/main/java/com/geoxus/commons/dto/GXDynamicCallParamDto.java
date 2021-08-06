package com.geoxus.commons.dto;

import com.geoxus.core.common.dto.GXBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态调用参数DTO
 * <p>
 * eg:
 * {@code
 * {
 * // javaType
 * // 不存在或者为空        微服务的调用方式是 XXXObj.xxxMethod(String username , Integer age)
 * // 如果存在或者不为空    微服务的调用方式是 XXXObj.xxxMethod(TestReqDto testReqDto)
 * "javaType":"com.geoxus.dto.PersonDto",
 * "attributes":[{
 * "fieldName":"permissions",
 * "javaType":"java.util.List",
 * "dataSource":"callback",
 * "callBackClassName":"com.geoxus.Service.TestService",
 * "callBackMethodName":"myTest"
 * },{
 * "fieldName":"userName",
 * "javaType":"java.lang.String",
 * "dataSource":"assign",
 * "fixedAssignedValue":1
 * },{
 * "fieldName":"nickName",
 * "javaType":"java.lang.String",
 * "dataSource":"token",
 * "sourceFieldName":"nickName"
 * }]
 * }
 * }
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GXDynamicCallParamDto extends GXBaseDto {
    /**
     * java类型
     */
    private String javaType;

    /**
     * 具体参数信息
     */
    private List<GXDynamicCallParamAttributeDto> attributes = new ArrayList<>(0);
}