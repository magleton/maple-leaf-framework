package cn.gaple.extension.test.customer.app.extension;

import cn.gaple.extension.test.customer.client.AddCustomerCmd;
import cn.gaple.extension.test.customer.client.CustomerDto;
import cn.gaple.extension.test.customer.domain.CustomerEntity;
import cn.maple.core.framework.util.GXSpringContextUtils;
import org.springframework.stereotype.Component;

@Component
public class CustomerConvertor {
    public CustomerEntity clientToEntity(Object clientObject) {
        AddCustomerCmd addCustomerCmd = (AddCustomerCmd) clientObject;
        CustomerDto customerDto = addCustomerCmd.getCustomerDTO();
        CustomerEntity customerEntity = GXSpringContextUtils.getBean(CustomerEntity.class);
        customerEntity.setCompanyName(customerDto.getCompanyName());
        customerEntity.setCustomerType(customerDto.getCustomerType());
        customerEntity.setBizScenario(addCustomerCmd.getBizScenario());
        return customerEntity;
    }
}
