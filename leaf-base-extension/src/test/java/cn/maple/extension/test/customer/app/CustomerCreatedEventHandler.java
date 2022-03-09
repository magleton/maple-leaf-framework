package cn.maple.extension.test.customer.app;

import cn.maple.extension.test.customer.client.CustomerCreatedEvent;
import cn.maple.core.framework.util.GXResultUtils;

public class CustomerCreatedEventHandler {
    public GXResultUtils<String> execute(CustomerCreatedEvent customerCreatedEvent) {
        System.out.println("customerCreatedEvent processed");
        return null;
    }
}
