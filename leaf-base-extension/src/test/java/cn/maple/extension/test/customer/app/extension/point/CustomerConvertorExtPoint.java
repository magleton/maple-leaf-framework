package cn.maple.extension.test.customer.app.extension.point;

import cn.maple.extension.GXExtensionPoint;
import cn.maple.extension.test.customer.client.AddCustomerCmd;
import cn.maple.extension.test.customer.domain.CustomerEntity;

public interface CustomerConvertorExtPoint extends GXExtensionPoint {
    CustomerEntity clientToEntity(AddCustomerCmd addCustomerCmd);
}
