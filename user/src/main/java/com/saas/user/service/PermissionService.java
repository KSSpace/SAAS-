package com.saas.user.service;

import com.saas.user.Permission;
import com.saas.user.exception.NotFoundException;
import com.saas.user.repository.PermissionRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Permission> allPermission() throws NotFoundException {
        List<Permission> perms = permissionRepository.findAll();
        if(perms == null){
            throw new NotFoundException("No permission is found.");
        }
        return perms;
    }

    public List<Permission> findPermissionsByTenantId(ObjectId tenantId) throws NotFoundException {
        List<Permission> perms = permissionRepository.findPermissionsByTenantId(tenantId);
        if (perms == null) {
            throw new NotFoundException("No permission is found.");
        }
        return perms;
    }

    public Permission findPermissionByPermissionId(ObjectId tenantId, String permId) throws NotFoundException {
        Permission perm = permissionRepository.findPermissionByTenantIdAndPermId(tenantId, permId);
        if (perm == null){
            throw new NotFoundException("The permission Id " + permId + " is not found.");
        }
        return perm;
    }

    public Permission createPermission(ObjectId tenantId, Permission perm) {
        Permission permission = permissionRepository.insert(new Permission(perm.getPermId(), perm.getPermName(), perm.getPermDesc(), tenantId));
        return permissionRepository.save(permission);
    }

    public Permission deletePermissionByPermId(ObjectId tenantId, String permId) throws NotFoundException {
        findPermissionByPermissionId(tenantId, permId);
        Permission permission = permissionRepository.deletePermissionByTenantIdAndPermId(tenantId, permId);
        return permission;
    }

    public Permission updatePermissionByPermId(ObjectId tenantId, String permId, Permission newPerm) throws NotFoundException {
        Permission exPerm = permissionRepository.findPermissionByTenantIdAndPermId(tenantId, permId);
        if (exPerm == null){
            throw new NotFoundException("The permission Id " + permId + " is not found.");
        }
        exPerm.setPermId(newPerm.getPermId());
        exPerm.setPermName(newPerm.getPermName());
        exPerm.setPermDesc(newPerm.getPermDesc());
        return permissionRepository.save(exPerm);
    }
}