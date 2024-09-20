package com.saas.user.controller;

import com.saas.user.Role;
import com.saas.user.exception.NotFoundException;
import com.saas.user.service.RoleService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/user/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/{tenantId}")
    public ResponseEntity<List<Role>> getRolesByTenantId(@PathVariable ObjectId tenantId) throws NotFoundException {
        return new ResponseEntity<List<Role>>(roleService.findRolesByTenantId(tenantId), HttpStatus.OK);
    }

    @GetMapping("/{tenantId}/{roleId}")
    public ResponseEntity<Role> getRoleByRoleId(@PathVariable ObjectId tenantId, @PathVariable String roleId) throws NotFoundException {
        return new ResponseEntity<Role>(roleService.findRoleByRoleId(tenantId, roleId), HttpStatus.OK);
    }

    @PostMapping("/{tenantId}/create")
    public ResponseEntity<Role> createRole(@PathVariable ObjectId tenantId, @RequestBody Role role){
        return new ResponseEntity<Role>(roleService.createRole(tenantId, role), HttpStatus.CREATED);
    }

    @DeleteMapping("/{tenantId}/delete_{roleId}")
    public ResponseEntity deleteRole(@PathVariable ObjectId tenantId, @PathVariable String roleId) throws NotFoundException {
        roleService.deleteRoleByRoleId(tenantId, roleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{tenantId}/update_{roleId}")
    public ResponseEntity<Role> updateRole(@PathVariable ObjectId tenantId, @PathVariable String roleId, @RequestBody Role role) throws NotFoundException {
        return new ResponseEntity<Role>(roleService.updateRoleByRoleId(tenantId, roleId, role), HttpStatus.OK);
    }

    @PutMapping("/{tenantId}/assignpermission_{roleId}")
    public ResponseEntity<Role> assignPermission(@PathVariable ObjectId tenantId, @PathVariable String roleId, @RequestBody List<String> permIds) throws NotFoundException {
        return new ResponseEntity<Role>(roleService.assignPermissionByRoleId(tenantId, roleId, permIds), HttpStatus.OK);
    }
}
