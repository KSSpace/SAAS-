package com.saas.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document(collection = "role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    private ObjectId id;
    private String roleId;
    private String roleName;
    private String roleDesc;
    private List<String> permissionIds;
    private ObjectId tenantId;

    public Role(String roleId, String roleName, String roleDesc, List<String> permissionIds, ObjectId tenantId){
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleDesc = roleDesc;
        this.permissionIds = permissionIds;
        this.tenantId = tenantId;
    }
}
