package cn.gaple.extension.test.customer.client;

import cn.maple.core.framework.dto.GXBaseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetOneCustomerQry extends GXBaseData {
    private long customerId;
    
    private String companyName;
}
