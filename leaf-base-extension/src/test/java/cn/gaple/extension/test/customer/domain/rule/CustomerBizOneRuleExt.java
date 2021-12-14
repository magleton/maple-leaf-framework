package cn.gaple.extension.test.customer.domain.rule;

import cn.gaple.extension.GXExtension;
import cn.gaple.extension.test.customer.client.GXConstants;
import cn.gaple.extension.test.customer.domain.CustomerEntity;
import cn.gaple.extension.test.customer.domain.SourceType;
import cn.maple.core.framework.exception.GXBusinessException;

@GXExtension(bizId = GXConstants.BIZ_1)
public class CustomerBizOneRuleExt implements CustomerRuleExtPt {
    @Override
    public boolean addCustomerCheck(CustomerEntity customerEntity) {
        if (SourceType.AD == customerEntity.getSourceType()) {
            throw new GXBusinessException("Sorry, Customer from advertisement can not be added in this period");
        }
        return true;
    }
}
