package com.saas.user.controller;

import com.saas.user.Permission;
import com.saas.user.exception.NotFoundException;
import com.saas.user.service.PermissionService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/user/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public ResponseEntity<List<Permission>> getAllPermissions() throws NotFoundException {
        return new ResponseEntity<List<Permission>>(permissionService.allPermission(), HttpStatus.OK);
    }

    @GetMapping("/{tenantId}")
    public ResponseEntity<List<Permission>> getPermissionsByTenantId(@PathVariable ObjectId tenantId) throws NotFoundException {
        return new ResponseEntity<List<Permission>>(permissionService.findPermissionsByTenantId(tenantId), HttpStatus.OK);
    }

    @GetMapping("/{tenantId}/{permId}")
    public ResponseEntity<Permission> getPermissionByTenantIdAndPermissionId(@PathVariable ObjectId tenantId, @PathVariable String permId) throws NotFoundException {
        return new ResponseEntity<Permission>(permissionService.findPermissionByPermissionId(tenantId, permId), HttpStatus.OK);
    }

    @PostMapping("/{tenantId}/create")
    public ResponseEntity createPermission(@PathVariable ObjectId tenantId, @RequestBody Permission perm){
        return new ResponseEntity<>(permissionService.createPermission(tenantId, perm), HttpStatus.CREATED);
    }

    @DeleteMapping("/{tenantId}/delete_{permId}")
    public ResponseEntity deletePermission(@PathVariable ObjectId tenantId, @PathVariable String permId) throws NotFoundException {
        permissionService.deletePermissionByPermId(tenantId, permId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{tenantId}/update_{permId}")
    public ResponseEntity updatePermission(@PathVariable ObjectId tenantId, @PathVariable String permId,@RequestBody Permission newPerm) throws NotFoundException {
        permissionService.updatePermissionByPermId(tenantId, permId, newPerm);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
