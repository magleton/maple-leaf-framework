package com.geoxus;

import com.geoxus.core.datasource.annotation.GXDataSource;
import com.geoxus.entity.UserEntity;
import com.geoxus.mongodb.annotation.GXMongoDataSourceAnnotation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApp.class)
@EnableAspectJAutoProxy
public class MongoTest {
    @Resource
    private MongoTemplate mongoTemplate;

    @Test
    @GXMongoDataSourceAnnotation("framework")
    @GXDataSource("framework")
    public void test() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("chenchen");
        userEntity.setPassWord("222222");
        userEntity.setId(22222L);
        UserEntity insert = mongoTemplate.insert(userEntity);
        System.out.println(insert);
    }
}
