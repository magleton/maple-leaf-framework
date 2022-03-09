package cn.gaple.extension.test.customer.domain.rule;

import cn.gaple.extension.GXExtension;
import cn.gaple.extension.test.customer.client.GXConstants;
import cn.gaple.extension.test.customer.domain.CustomerEntity;
import org.springframework.stereotype.Component;

@GXExtension(bizId = GXConstants.BIZ_2)
@Component
public class CustomerBizTwoRuleExt implements CustomerRuleExtPoint {
    @Override
    public boolean addCustomerCheck(CustomerEntity customerEntity) {
        //Any Customer can be added
        return true;
    }
}
