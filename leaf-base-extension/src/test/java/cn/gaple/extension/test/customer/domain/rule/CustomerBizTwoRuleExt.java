package cn.gaple.extension.test.customer.domain.rule;

import cn.gaple.extension.GXExtension;
import cn.gaple.extension.test.customer.client.GXConstants;
import cn.gaple.extension.test.customer.domain.CustomerEntity;

@GXExtension(bizId = GXConstants.BIZ_2)
public class CustomerBizTwoRuleExt implements CustomerRuleExtPt {
    @Override
    public boolean addCustomerCheck(CustomerEntity customerEntity) {
        //Any Customer can be added
        return true;
    }
}
