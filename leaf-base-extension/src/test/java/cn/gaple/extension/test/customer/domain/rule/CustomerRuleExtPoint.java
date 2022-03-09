package cn.gaple.extension.test.customer.domain.rule;

import cn.gaple.extension.GXExtensionPoint;
import cn.gaple.extension.test.customer.domain.CustomerEntity;

public interface CustomerRuleExtPoint extends GXExtensionPoint {
    boolean addCustomerCheck(CustomerEntity customerEntity);

    default void customerUpgradePolicy(CustomerEntity customerEntity) {
        //Nothing special
    }
}
