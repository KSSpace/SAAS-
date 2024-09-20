package com.saas.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rolepermission")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Id
    private ObjectId id;
    private String permId;
    private String permName;
    private String permDesc;
    private ObjectId tenantId;

    public Permission(String permId, String permName, String permDesc, ObjectId tenantId){
        this.permId = permId;
        this.permName = permName;
        this.permDesc = permDesc;
        this.tenantId = tenantId;
    }
}