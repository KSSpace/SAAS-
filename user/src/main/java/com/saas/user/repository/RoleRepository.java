package com.saas.user.repository;

import com.saas.user.Role;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends MongoRepository<Role, ObjectId> {
    @Query("{'tenantId': ?0}")
    List<Role> findRolesByTenantId(ObjectId tenantId);

    @Query("{'tenantId': ?0, 'roleId': ?1}")
    Role findRoleByTenantIdAndRoleId(ObjectId tenantId, String roleId);

    Role deleteRoleByTenantIdAndRoleId(ObjectId tenantId, String roleId);
}