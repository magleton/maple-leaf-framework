package com.geoxus.order.repository.primary;

import com.geoxus.order.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Component;

@Component
@EnableMongoRepositories(basePackages = "com.geoxus.order", mongoTemplateRef = "primaryMongoTemplate")
public interface UserRepository extends MongoRepository<UserEntity, String> {
}
