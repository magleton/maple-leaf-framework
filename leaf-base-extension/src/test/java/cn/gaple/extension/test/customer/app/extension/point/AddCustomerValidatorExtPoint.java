package cn.gaple.extension.test.customer.app.extension.point;

import cn.gaple.extension.GXExtensionPoint;
import cn.gaple.extension.test.customer.client.AddCustomerCmd;

public interface AddCustomerValidatorExtPoint extends GXExtensionPoint {
    void validate(AddCustomerCmd addCustomerCmd);
}
