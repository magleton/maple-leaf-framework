package cn.gaple.extension.test.customer.app.extension.point;

import cn.gaple.extension.GXExtensionPoint;
import cn.gaple.extension.test.customer.client.AddCustomerCmd;
import cn.gaple.extension.test.customer.domain.CustomerEntity;

public interface CustomerConvertorExtPoint extends GXExtensionPoint {
    CustomerEntity clientToEntity(AddCustomerCmd addCustomerCmd);
}
