package cn.maple.extension.test.customer.infrastructure;

import cn.maple.core.framework.dto.GXBaseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerDo extends GXBaseData {
    private String customerId;
    private String memberId;
    private String globalId;
    private String companyName;
    private String source;
    private String companyType;
}