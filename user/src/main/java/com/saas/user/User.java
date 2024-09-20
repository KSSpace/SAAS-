package com.saas.user;

import com.saas.user.utils.TenantType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private ObjectId id;
    private String userId;
    private String userName;
    private String email;
    private String phoneNum;
    private String password;
    private String salt; // Salt used for hashing the password
    private String otp; // One Time Password for verification purposes
    private LocalDateTime otpExpiry; // Expiration time of the OTP
    private boolean verified; // Field to track verification status
    private List<String> roleIds;
    private ObjectId tenantId;
    
    public User(String userId, String userName, String email, String phoneNum, String password, List<String> roleIds, ObjectId tenantId) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.password = password;
        this.roleIds = roleIds;
        this.tenantId = tenantId;
    }

}
