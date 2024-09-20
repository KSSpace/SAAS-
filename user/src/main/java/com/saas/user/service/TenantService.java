package com.saas.user.service;

import com.saas.user.Permission;
import com.saas.user.Tenant;
import com.saas.user.exception.NotFoundException;
import com.saas.user.exception.RequestException;
import com.saas.user.repository.TenantRepository;
import com.saas.user.utils.TenantType;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    public boolean checkTenantTypeIsAdmin(ObjectId id) {
        TenantType type = tenantRepository.findTenantById(id).getTenantType();
        return (type == TenantType.ADMINISTRATOR);
    }

    public List<Tenant> allTenants(ObjectId id) throws NotFoundException {
        if (!checkTenantTypeIsAdmin(id)){
            throw new RequestException("No access allowed.");
        }
        List<Tenant> tenants = tenantRepository.findAll();
        if(tenants == null){
            throw new NotFoundException("No tenant is found.");
        }
        return tenants;
    }

    public Tenant findTenantByTenantId(ObjectId id, String TenantId) throws NotFoundException {
        if (!checkTenantTypeIsAdmin(id)){
            throw new RequestException("No access allowed.");
        }
        Tenant tenant = tenantRepository.findTenantByTenantId(TenantId);
        if(tenant == null){
            throw new NotFoundException("Tenant is not found with the id: "+ TenantId);
        }
        return tenant;
    }

    public Tenant registerTenant(ObjectId id, String tenantId, String tenantName, TenantType tenantType) {
        if (!checkTenantTypeIsAdmin(id)){
            throw new RequestException("No access allowed.");
        }
        Tenant tenant = tenantRepository.insert(new Tenant(tenantId, tenantName, tenantType));
        return tenantRepository.save(tenant);
    }

    public void deleteAllTenants(ObjectId id) {
        if (!checkTenantTypeIsAdmin(id)){
            throw new RequestException("No access allowed.");
        }
        tenantRepository.deleteAll();
    }

    public void deleteTenantByTenantId(ObjectId id, ObjectId deleteid) throws NotFoundException {
        if (!checkTenantTypeIsAdmin(id)){
            throw new RequestException("No access allowed.");
        }
        Tenant tenant = tenantRepository.findById(deleteid).get();
        if (tenant == null){
            throw new NotFoundException("The tenant updated is not found.");
        }
        tenantRepository.deleteTenantById(deleteid);
        return;
    }

    public Tenant updateTenant(ObjectId id, ObjectId updateid, Tenant newTenant) throws NotFoundException {
        if (!checkTenantTypeIsAdmin(id)){
            throw new RequestException("No access allowed.");
        }
        Tenant exTenant = tenantRepository.findById(updateid).get();
        if (exTenant == null){
            throw new NotFoundException("The tenant updated is not found.");
        }
        exTenant.setTenantId(newTenant.getTenantId());
        exTenant.setTenantName(newTenant.getTenantName());
        return tenantRepository.save(exTenant);
    }
}
