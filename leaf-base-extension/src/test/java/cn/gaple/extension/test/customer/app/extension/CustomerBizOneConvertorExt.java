package cn.gaple.extension.test.customer.app.extension;

import cn.gaple.extension.GXExtension;
import cn.gaple.extension.test.customer.app.extension.point.CustomerConvertorExtPoint;
import cn.gaple.extension.test.customer.client.AddCustomerCmd;
import cn.gaple.extension.test.customer.client.CustomerDto;
import cn.gaple.extension.test.customer.client.GXConstants;
import cn.gaple.extension.test.customer.domain.CustomerEntity;
import cn.gaple.extension.test.customer.domain.SourceType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@GXExtension(bizId = GXConstants.BIZ_1)
@Component
public class CustomerBizOneConvertorExt implements CustomerConvertorExtPoint {
    @Resource
    private CustomerConvertor customerConvertor;//Composite basic convertor to do basic conversion

    @Override
    public CustomerEntity clientToEntity(AddCustomerCmd addCustomerCmd) {
        CustomerEntity customerEntity = customerConvertor.clientToEntity(addCustomerCmd);
        CustomerDto customerDTO = addCustomerCmd.getCustomerDTO();
        //In this business, AD and RFQ are regarded as different source
        if (GXConstants.SOURCE_AD.equals(customerDTO.getSource())) {
            customerEntity.setSourceType(SourceType.AD);
        }
        if (GXConstants.SOURCE_RFQ.equals(customerDTO.getSource())) {
            customerEntity.setSourceType(SourceType.RFQ);
        }
        return customerEntity;
    }
}
