package com.saas.user.repository;

import com.saas.user.Permission;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends MongoRepository<Permission, ObjectId> {

    @Query("{'tenantId': ?0}")
    List<Permission> findPermissionsByTenantId(ObjectId tenantId);

    @Query("{'tenantId': ?0, 'permId': ?1}")
    Permission findPermissionByTenantIdAndPermId(ObjectId tenantId, String permId);

    Permission deletePermissionByTenantIdAndPermId(ObjectId tenantId, String permId);
}
