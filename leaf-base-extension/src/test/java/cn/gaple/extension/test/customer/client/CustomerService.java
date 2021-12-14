package cn.gaple.extension.test.customer.client;

import cn.maple.core.framework.util.GXResultUtils;

public interface CustomerService {
    GXResultUtils<String> addCustomer(AddCustomerCmd addCustomerCmd);

    GXResultUtils<CustomerDto> getCustomer(GetOneCustomerQry getOneCustomerQry);
}
