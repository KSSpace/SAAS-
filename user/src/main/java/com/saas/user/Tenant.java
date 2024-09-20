package com.saas.user;

import com.saas.user.utils.TenantType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document(collection = "tenant")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {

    @Id
    private ObjectId id;
    private String tenantId;
    private String tenantName;
    @DocumentReference
    private List<User> userIds;
    private TenantType tenantType;

    public Tenant(String tId, String tName, TenantType tType){
        this.tenantId = tId;
        this.tenantName = tName;
        this.tenantType = tType;
    }

}
