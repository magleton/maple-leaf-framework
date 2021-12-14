package cn.gaple.extension;

import cn.gaple.extension.test.customer.client.AddCustomerCmd;
import cn.gaple.extension.test.customer.client.CustomerDto;
import cn.gaple.extension.test.customer.client.CustomerService;
import cn.gaple.extension.test.customer.client.GXConstants;
import cn.gaple.extension.test.customer.domain.CustomerType;
import cn.maple.core.framework.util.GXResultUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ExtTestApplication.class)
public class ExtensionTest {
    @Resource
    private CustomerService customerService;

    @Test
    public void testBiz1UseCase1Scenario1AddCustomerSuccess() {
        //1. Prepare
        AddCustomerCmd addCustomerCmd = new AddCustomerCmd();
        CustomerDto customerDTO = new CustomerDto();
        customerDTO.setCompanyName("alibaba");
        customerDTO.setSource(GXConstants.SOURCE_RFQ);
        customerDTO.setCustomerType(CustomerType.IMPORTANT);
        addCustomerCmd.setCustomerDTO(customerDTO);
        GXBizScenario scenario = GXBizScenario.valueOf(GXConstants.BIZ_1, GXConstants.USE_CASE_1, GXConstants.SCENARIO_1);
        addCustomerCmd.setBizScenario(scenario);

        //2. Execute
        GXResultUtils<String> response = customerService.addCustomer(addCustomerCmd);

        //3. Expect Success
        Assert.assertTrue(response != null);
    }

    @Test
    public void testBiz1UseCase1AddCustomerSuccess() {
        //1. Prepare
        AddCustomerCmd addCustomerCmd = new AddCustomerCmd();
        CustomerDto customerDTO = new CustomerDto();
        customerDTO.setCompanyName("alibaba");
        customerDTO.setSource(GXConstants.SOURCE_RFQ);
        customerDTO.setCustomerType(CustomerType.IMPORTANT);
        addCustomerCmd.setCustomerDTO(customerDTO);
        GXBizScenario scenario = GXBizScenario.valueOf(GXConstants.BIZ_1, GXConstants.USE_CASE_1);
        addCustomerCmd.setBizScenario(scenario);

        //2. Execute
        GXResultUtils<String> response = customerService.addCustomer(addCustomerCmd);

        //3. Expect Success
        Assert.assertTrue(response != null);
    }

    @Test
    public void testBiz1AddCustomerSuccess() {
        //1. Prepare
        AddCustomerCmd addCustomerCmd = new AddCustomerCmd();
        CustomerDto customerDTO = new CustomerDto();
        customerDTO.setCompanyName("jd");
        customerDTO.setSource(GXConstants.SOURCE_RFQ);
        customerDTO.setCustomerType(CustomerType.IMPORTANT);
        addCustomerCmd.setCustomerDTO(customerDTO);
        GXBizScenario scenario = GXBizScenario.valueOf(GXConstants.BIZ_1);
        addCustomerCmd.setBizScenario(scenario);

        //2. Execute
        GXResultUtils<String> response = customerService.addCustomer(addCustomerCmd);

        //3. Expect Success
        Assert.assertTrue(response != null);
    }
}
