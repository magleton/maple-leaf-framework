package cn.maple.extension.test.customer.domain.rule;

import cn.maple.extension.GXExtensionPoint;
import cn.maple.extension.test.customer.domain.CustomerEntity;

public interface CustomerRuleExtPoint extends GXExtensionPoint {
    boolean addCustomerCheck(CustomerEntity customerEntity);

    default void customerUpgradePolicy(CustomerEntity customerEntity) {
        //Nothing special
    }
}
