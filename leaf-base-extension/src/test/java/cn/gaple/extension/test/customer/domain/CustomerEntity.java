package cn.gaple.extension.test.customer.domain;

import cn.gaple.extension.GXBizScenario;
import cn.gaple.extension.GXExtensionExecutor;
import cn.gaple.extension.test.customer.domain.rule.CustomerRuleExtPt;
import cn.gaple.extension.test.customer.infrastructure.CustomerRepository;
import cn.maple.core.framework.annotation.GXEntity;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@GXEntity
@Data
public class CustomerEntity {
    private String companyName;

    private SourceType sourceType;

    private CustomerType customerType;

    private GXBizScenario bizScenario;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GXExtensionExecutor extensionExecutor;

    public CustomerEntity() {

    }

    public void addNewCustomer() {
        //Add customer policy
        extensionExecutor.execute(CustomerRuleExtPt.class, this.getBizScenario(), extension -> extension.addCustomerCheck(this));

        //Persist customer
        customerRepository.persist(this);
    }
}
