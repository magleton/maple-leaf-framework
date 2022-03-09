package cn.maple.extension.test.customer.app.extension.point;

import cn.maple.extension.GXExtensionPoint;
import cn.maple.extension.test.customer.client.AddCustomerCmd;

public interface AddCustomerValidatorExtPoint extends GXExtensionPoint {
    void validate(AddCustomerCmd addCustomerCmd);
}
