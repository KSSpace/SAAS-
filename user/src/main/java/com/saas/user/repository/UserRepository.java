package com.saas.user.repository;

import com.saas.user.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

    @Query("{'tenantId': ?0}")
    List<User> findUsersByTenantId(ObjectId tenantId);

    @Query("{'tenantId': ?0,'userId': ?1}")
    User findUsersByUserId(ObjectId tenantId, String userId);

    User deleteUserByTenantIdAndUserId(ObjectId tenantId, String userId);

    User findUserByTenantIdAndEmail(ObjectId tenantId, String email);

    User findUserByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByPhoneNum(String phoneNum);
}
