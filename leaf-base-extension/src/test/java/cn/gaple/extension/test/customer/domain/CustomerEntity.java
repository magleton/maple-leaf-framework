package cn.gaple.extension.test.customer.domain;

import cn.gaple.extension.GXBizScenario;
import cn.gaple.extension.GXExtensionExecutor;
import cn.gaple.extension.test.customer.domain.rule.CustomerRuleExtPoint;
import cn.gaple.extension.test.customer.infrastructure.CustomerRepository;
import cn.maple.core.framework.ddd.annotation.GXDomainEntity;
import lombok.Data;

@GXDomainEntity
@Data
public class CustomerEntity {
    private String companyName;

    private SourceType sourceType;

    private CustomerType customerType;

    private GXBizScenario bizScenario;

    private CustomerRepository customerRepository;

    private GXExtensionExecutor extensionExecutor;

    public CustomerEntity(GXExtensionExecutor extensionExecutor, CustomerRepository customerRepository) {
        this.extensionExecutor = extensionExecutor;
        this.customerRepository = customerRepository;
    }

    public void addNewCustomer() {
        //Add customer policy
        extensionExecutor.execute(CustomerRuleExtPoint.class, this.getBizScenario(), extension -> extension.addCustomerCheck(this));

        //Persist customer
        customerRepository.persist(this);
    }
}
