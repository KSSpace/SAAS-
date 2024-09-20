package com.saas.user.service;

import com.saas.user.Role;
import com.saas.user.Tenant;
import com.saas.user.User;
import com.saas.user.exception.*;
import com.saas.user.repository.RoleRepository;
import com.saas.user.repository.TenantRepository;
import com.saas.user.repository.UserRepository;
import com.saas.user.utils.*;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private EmailService emailService;

    public List<User> findAllUsers(ObjectId tenantId) throws NotFoundException{
        TenantType type = tenantRepository.findTenantById(tenantId).getTenantType();
        if (type != TenantType.ADMINISTRATOR){
            throw new RequestException("No access allowed.");
        }
        List<User> users = userRepository.findAll();
        if (users == null){
            throw new NotFoundException("No user is found.");
        }
        return users;
    }

    public List<User> findUsersByTenantId(ObjectId tenantId) throws NotFoundException {
        List<User> users = userRepository.findUsersByTenantId(tenantId);
        if (users == null){
            throw new NotFoundException("No user is found.");
        }
        return users;
    }

    public User findUserByUserId(ObjectId tenantId, String userId) throws NotFoundException {
        User user = userRepository.findUsersByUserId(tenantId, userId);
        if (user == null){
            throw new NotFoundException("The user " + userId + " is not found.");
        }
        return user;
    }

    public User registerUser(ObjectId tenantId, User newUser) {
        // Check for existing email and phone number
        if (userRepository.existsByEmail(newUser.getEmail())) {
            throw new UserAlreadyExistsException("A user with this email already exists.");
        }

        if (userRepository.existsByPhoneNum(newUser.getPhoneNum())) {
            throw new UserAlreadyExistsException("A user with this phone number already exists.");
        }

        // Validate password strength
        String passwordValidationResult = PasswordUtils.validatePasswordStrength(newUser.getPassword());
        if (!"Password is valid".equals(passwordValidationResult)) {
            throw new IllegalArgumentException("Invalid password: " + passwordValidationResult);
        }

        // Generate a salt and hash the password
        String salt = SecurityUtils.generateSalt();
        String hashedPassword = SecurityUtils.hashPassword(newUser.getPassword(), salt);
        newUser.setPassword(hashedPassword);
        newUser.setSalt(salt);
        newUser.setTenantId(tenantId);
        newUser.setVerified(false); // Set the user as unverified initially

        // Insert the new user in the database
        User savedUser = userRepository.insert(newUser);

        // Update the Tenant with the new user
        mongoTemplate.update(Tenant.class)
            .matching(Criteria.where("id").is(tenantId))
            .apply(new Update().push("userIds").value(savedUser.getId()))
            .first();

        return savedUser;
    }
    
    public void verifyOtp(ObjectId tenantId, String userEmail, String submittedOtp) throws NotFoundException, InvalidOtpException {
        User user = userRepository.findUserByTenantIdAndEmail(tenantId, userEmail);
        if (user == null) {
            throw new NotFoundException("User not found with email: " + userEmail);
        }

        if (user.getOtp() != null && user.getOtp().equals(submittedOtp) &&
            user.getOtpExpiry() != null && user.getOtpExpiry().isAfter(LocalDateTime.now())) {
            // OTP is correct and not expired
            user.setVerified(true); // Mark user as verified
            user.setOtp(null); // Clear the OTP
            user.setOtpExpiry(null); // Clear the OTP expiry
            userRepository.save(user);
        } else {
            // OTP is incorrect or expired
            throw new InvalidOtpException("Invalid or expired OTP.");
        }
    }

    public void sendOtpToUserEmail(ObjectId tenantId, String userEmail, OtpPurpose purpose) throws NotFoundException {
        String otp = OtpUtils.generateOtp();
        LocalDateTime otpExpiry = LocalDateTime.now().plusMinutes(15); // Set OTP expiry time

        User user = userRepository.findUserByTenantIdAndEmail(tenantId, userEmail);
        if (user == null) {
            throw new NotFoundException("User not found with email: " + userEmail);
        }

        user.setOtp(otp);
        user.setOtpExpiry(otpExpiry);
        userRepository.save(user); // Save OTP and expiry in the database

        String subject = "Your OTP for " + purpose.getDescription();
        String message = "Your OTP is: " + otp;
        emailService.sendOtpMessage(userEmail, subject, message); // Send OTP via email
    }


    public User loginUser(String usernameOrEmail, String password) throws NotFoundException, AuthenticationException {
        User user = userRepository.findUserByEmail(usernameOrEmail);
        if (user == null) {
            throw new NotFoundException("User not found with provided email.");
        }

        if (!user.isVerified()) {
            throw new AuthenticationException("User account is not verified.");
        }

        String hashedSubmittedPassword = SecurityUtils.hashPassword(password, user.getSalt());
        if (!hashedSubmittedPassword.equals(user.getPassword())) {
            throw new AuthenticationException("Invalid password.");
        }

        // User is verified and password is correct, proceed with login

        return user;
    }

    public void resetPassword(ObjectId tenantId, String userEmail, String submittedOtp, String newPassword) throws NotFoundException, InvalidOtpException, WeakPasswordException {
        User user = userRepository.findUserByTenantIdAndEmail(tenantId, userEmail);
        if (user == null) {
            throw new NotFoundException("User not found.");
        }

        // Validate the password strength
        String passwordValidationResult = PasswordUtils.validatePasswordStrength(newPassword);
        if (!"Password is valid".equals(passwordValidationResult)) {
            throw new WeakPasswordException("Weak password: " + passwordValidationResult);
        }

        // Verifying the OTP
        if (user.getOtp() != null && user.getOtp().equals(submittedOtp) &&
            user.getOtpExpiry() != null && user.getOtpExpiry().isAfter(LocalDateTime.now())) {

            String salt = SecurityUtils.generateSalt();
            String hashedPassword = SecurityUtils.hashPassword(newPassword, salt);
            user.setPassword(hashedPassword);
            user.setSalt(salt); // Save the new salt
            user.setOtp(null); // Clear the OTP
            user.setOtpExpiry(null); // Clear the OTP expiry
            userRepository.save(user);
        } else {
            // OTP is incorrect or expired
            throw new InvalidOtpException("Invalid or expired OTP.");
        }
    }

    public User updateUser(ObjectId tenantId, String userId, User newUser) throws NotFoundException {
        User exUser = userRepository.findUsersByUserId(tenantId, userId);
        if (exUser == null) {
            throw new NotFoundException("The user " + userId + " is not found.");
        }
        exUser.setUserId(newUser.getUserId());
        exUser.setUserName(newUser.getUserName());
        exUser.setPhoneNum(newUser.getPhoneNum());
        exUser.setRoleIds(newUser.getRoleIds());
        return userRepository.save(exUser);
    }

    public User deleteUserByUserId(ObjectId tenantId, String userId) throws NotFoundException {
        User exUser = userRepository.findUsersByUserId(tenantId, userId);
        if (exUser == null) {
            throw new NotFoundException("The user " + userId + " is not found.");
        }
        return userRepository.deleteUserByTenantIdAndUserId(tenantId, userId);
    }

    //只保存RoleId
    public User assignRolesByRoleId(ObjectId tenantId, String userId, List<String> roleIds) throws NotFoundException {
        User user = userRepository.findUsersByUserId(tenantId, userId);
        if (user == null){
            throw new NotFoundException("The userId " + userId + " is not found.");
        }
        //如果前端能让用户只能从已有的Role里面选的话 应该不会有这个问题
        for (String roleId : roleIds) {
            if (roleRepository.findRoleByTenantIdAndRoleId(tenantId, roleId) == null){
                throw new NotFoundException("The roleId " + roleId + "is not found.");
            }
        }
        user.setRoleIds(roleIds);
        return userRepository.save(user);
    }

    //通过整个Role保存roleId
    public User assignRolesByRole(ObjectId tenantId, String userId, List<Role> roles){
        User user = userRepository.findUsersByUserId(tenantId, userId);
        List<String> roleIds = null;
        //如果前端让用户只能从已有的Role里面选的话 应该不会有这个问题
        for (Role role: roles) {
            roleIds.add(role.getRoleId());
        }
        user.setRoleIds(roleIds);
        return userRepository.save(user);
    }

    // For platform admin to update user's new tenant id
    public User updateUserTenantId(ObjectId id, User newUser) {
        User exUser = userRepository.findById(id).get();
        exUser.setUserId(null);
        exUser.setRoleIds(null);
        exUser.setTenantId(newUser.getTenantId());
        return userRepository.save(exUser);
    }
}
