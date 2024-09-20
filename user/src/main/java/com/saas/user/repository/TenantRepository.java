package com.saas.user.repository;

import com.saas.user.Tenant;
import com.saas.user.utils.TenantType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends MongoRepository<Tenant, ObjectId> {
    Tenant findTenantByTenantId(String tenantId);

    Tenant deleteTenantById(ObjectId id);

    Tenant findTenantById(ObjectId id);
}
