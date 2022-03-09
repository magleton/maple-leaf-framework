package cn.maple.extension.register;

import cn.maple.extension.GXExtension;
import org.springframework.stereotype.Component;

@GXExtension(bizId = "A")
@Component
public class SomeExtensionA implements GXSomeExtPoint {
    @Override
    public void doSomeThing() {
        System.out.println("SomeExtensionA::doSomething");
    }
}
