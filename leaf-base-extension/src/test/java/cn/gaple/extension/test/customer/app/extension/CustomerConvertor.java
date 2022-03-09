package cn.gaple.extension.test.customer.app.extension;

import cn.gaple.extension.GXExtensionExecutor;
import cn.gaple.extension.test.customer.client.AddCustomerCmd;
import cn.gaple.extension.test.customer.client.CustomerDto;
import cn.gaple.extension.test.customer.domain.CustomerEntity;
import cn.gaple.extension.test.customer.infrastructure.CustomerRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CustomerConvertor {
    @Resource
    private GXExtensionExecutor extensionExecutor;

    @Resource
    private CustomerRepository customerRepository;

    public CustomerEntity clientToEntity(Object clientObject) {
        AddCustomerCmd addCustomerCmd = (AddCustomerCmd) clientObject;
        CustomerDto customerDto = addCustomerCmd.getCustomerDTO();
        CustomerEntity customerEntity = new CustomerEntity(extensionExecutor, customerRepository);
        customerEntity.setCompanyName(customerDto.getCompanyName());
        customerEntity.setCustomerType(customerDto.getCustomerType());
        customerEntity.setBizScenario(addCustomerCmd.getBizScenario());
        return customerEntity;
    }
}
