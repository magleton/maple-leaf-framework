package cn.gaple.extension.test.customer.infrastructure;

import cn.gaple.extension.test.customer.domain.CustomerEntity;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepository {
    public void persist(CustomerEntity customerEntity) {
        System.out.println("Persist customer to DB : " + customerEntity);
    }
}
