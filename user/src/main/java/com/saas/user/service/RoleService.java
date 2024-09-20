package com.saas.user.service;

import com.saas.user.Permission;
import com.saas.user.Role;
import com.saas.user.exception.NotFoundException;
import com.saas.user.repository.PermissionRepository;
import com.saas.user.repository.RoleRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    public List<Role> findRolesByTenantId(ObjectId tenantId) throws NotFoundException {
        List<Role> roles = roleRepository.findRolesByTenantId(tenantId);
        if (roles == null){
            throw new NotFoundException("No role is found.");
        }
        return roles;
    }

    public Role findRoleByRoleId(ObjectId tenantId, String roleId) throws NotFoundException {
        Role role = roleRepository.findRoleByTenantIdAndRoleId(tenantId, roleId);
        if (role == null){
            throw new NotFoundException("The role " + roleId + " is not found.");
        }
        return role;
    }

    public Role createRole(ObjectId tenantId, Role newRole){
        Role role = roleRepository.insert(new Role(newRole.getRoleId(), newRole.getRoleName(), newRole.getRoleDesc(), newRole.getPermissionIds(), tenantId));
        return roleRepository.save(role);
    }

    public void deleteRoleByRoleId(ObjectId tenantId, String roleId) throws NotFoundException {
        Role role = findRoleByRoleId(tenantId, roleId);
        roleRepository.deleteRoleByTenantIdAndRoleId(tenantId, roleId);
        return;
    }

    public Role updateRoleByRoleId(ObjectId tenantId, String roleId, Role newRole) throws NotFoundException {
        Role exRole = roleRepository.findRoleByTenantIdAndRoleId(tenantId, roleId);
        if (exRole == null){
            throw new NotFoundException("The role " + roleId + " is not found.");
        }
        exRole.setRoleId(newRole.getRoleId());
        exRole.setRoleName(newRole.getRoleName());
        exRole.setRoleDesc(newRole.getRoleDesc());
        return roleRepository.save(exRole);
    }

    public Role assignPermissionByRoleId(ObjectId tenantId, String roleId, List<String> permIds) throws NotFoundException {
        Role role = roleRepository.findRoleByTenantIdAndRoleId(tenantId, roleId);
        if (role == null){
            throw new NotFoundException("The roleId " + roleId + " is not found.");
        }
        for (String permId: permIds){
            if(permissionRepository.findPermissionByTenantIdAndPermId(tenantId, permId) == null){
                throw new NotFoundException("The permissionId " + permId + "is not found.");
            }
        }
        role.setPermissionIds(permIds);
        return roleRepository.save(role);
    }
}
