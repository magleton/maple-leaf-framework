package cn.maple.extension.register;

import cn.maple.extension.GXExtension;
import org.springframework.stereotype.Component;

@GXExtension(bizId = "B")
@Component
public class SomeExtensionB implements GXSomeExtPoint {
    @Override
    public void doSomeThing() {
        System.out.println("SomeExtensionB::doSomething");
    }
}
