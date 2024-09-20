package com.saas.user.controller;

import com.saas.user.Permission;
import com.saas.user.Tenant;
import com.saas.user.exception.NotFoundException;
import com.saas.user.service.TenantService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/tenant")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @GetMapping("/{id}/allTenants")
    public ResponseEntity<List<Tenant>> getAllTenants(@PathVariable ObjectId id) throws NotFoundException {
        return new ResponseEntity<List<Tenant>>(tenantService.allTenants(id),HttpStatus.OK);
    }

    @GetMapping("/{id}/getTenant")
    public ResponseEntity<Tenant> getTenantByTenantId(@PathVariable ObjectId id, @RequestBody String tenantId) throws NotFoundException {
        return new ResponseEntity<>(tenantService.findTenantByTenantId(id, tenantId), HttpStatus.OK);
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<Tenant> registerTenant(@PathVariable ObjectId id, @RequestBody Tenant tenant){
        return new ResponseEntity<>(tenantService.registerTenant(id, tenant.getTenantId(), tenant.getTenantName(), tenant.getTenantType()), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/deleteAll")
    public ResponseEntity deleteAllTenants(@PathVariable ObjectId id){
        tenantService.deleteAllTenants(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}/delete_{deleteid}")
    public ResponseEntity deleteTenantByTenantId(@PathVariable ObjectId id, @PathVariable ObjectId deleteid) throws NotFoundException {
        tenantService.deleteTenantByTenantId(id, deleteid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/update_{updateid}")
    public ResponseEntity updateTenant(@PathVariable ObjectId id, @PathVariable ObjectId updateid, @RequestBody Tenant newTenant) throws NotFoundException {
        tenantService.updateTenant(id, updateid, newTenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
