package cn.gaple.extension.test.customer.app;

import cn.gaple.extension.test.customer.client.CustomerDto;
import cn.gaple.extension.test.customer.client.GetOneCustomerQry;
import cn.maple.core.framework.util.GXResultUtils;
import org.springframework.stereotype.Component;

@Component
public class GetOneCustomerQryExe {
    public GXResultUtils<CustomerDto> execute(GetOneCustomerQry getOneCustomerQry) {
        return null;
    }
}
