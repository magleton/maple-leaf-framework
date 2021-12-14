package cn.gaple.extension.register;

import cn.gaple.extension.GXExtension;
import org.springframework.stereotype.Component;

@GXExtension(bizId = "A")
@Component
public class SomeExtensionA implements GXSomeExtPoint {
    @Override
    public void doSomeThing() {
        System.out.println("SomeExtensionA::doSomething");
    }
}
