package cn.maple.extension.test.customer.app.extension;

import cn.maple.extension.GXExtension;
import cn.maple.extension.test.customer.app.extension.point.AddCustomerValidatorExtPoint;
import cn.maple.extension.test.customer.client.AddCustomerCmd;
import cn.maple.extension.test.customer.client.GXConstants;
import org.springframework.stereotype.Component;

@GXExtension(bizId = GXConstants.BIZ_1, useCase = GXConstants.USE_CASE_1)
@Component
public class AddCustomerBiz1UseCase1Validator implements AddCustomerValidatorExtPoint {
    public void validate(AddCustomerCmd addCustomerCmd) {
        System.out.println("Do validation for Biz_One's Use_Case_One");
    }
}
