package cn.gaple.extension.test.customer.app.extension;

import cn.gaple.extension.GXExtension;
import cn.gaple.extension.test.customer.app.extension.point.AddCustomerValidatorExtPoint;
import cn.gaple.extension.test.customer.client.AddCustomerCmd;
import cn.gaple.extension.test.customer.client.GXConstants;
import org.springframework.stereotype.Component;

@GXExtension(bizId = GXConstants.BIZ_1, useCase = GXConstants.USE_CASE_1, scenario = GXConstants.SCENARIO_1)
@Component
public class AddCustomerBiz1UseCase1Scenario1Validator implements AddCustomerValidatorExtPoint {
    public void validate(AddCustomerCmd addCustomerCmd) {
        System.out.println("Do validation for Biz_One's Use_Case_One's Scenario_One");
    }
}
