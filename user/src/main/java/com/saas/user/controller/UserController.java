package com.saas.user.controller;

import com.saas.user.Role;
import com.saas.user.User;
import com.saas.user.exception.NotFoundException;
import com.saas.user.exception.RequestException;
import com.saas.user.exception.UserAlreadyExistsException;
import com.saas.user.exception.WeakPasswordException;
import com.saas.user.service.UserService;

import com.saas.user.utils.OtpPurpose;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
import com.saas.user.dto.EmailDTO;
import com.saas.user.dto.OtpVerificationRequest;
import com.saas.user.dto.LoginRequest;
import com.saas.user.dto.EmailRecoveryRequest;
import com.saas.user.dto.ResetPasswordRequest;
import com.saas.user.exception.InvalidOtpException;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{tenantId}/allUsers")
    public ResponseEntity<Map<String, Object>> getAllUsers(@PathVariable ObjectId tenantId) throws NotFoundException {
        Map<String, Object> response = new HashMap<>();
        response.put("users", userService.findAllUsers(tenantId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{tenantId}")
    public ResponseEntity<Map<String, Object>> getUsersByTenantId(@PathVariable ObjectId tenantId) throws NotFoundException {
        Map<String, Object> response = new HashMap<>();
        response.put("users", userService.findUsersByTenantId(tenantId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/{tenantId}/{userId}")
    public ResponseEntity<User> getUserByUserId(@PathVariable ObjectId tenantId, @PathVariable String userId) throws NotFoundException {
        return new ResponseEntity<>(userService.findUserByUserId(tenantId, userId), HttpStatus.OK);
    }

    @PostMapping("/{tenantId}/register")
    public ResponseEntity<User> registerUser(@PathVariable ObjectId tenantId, @RequestBody User user){
        return new ResponseEntity<User>(userService.registerUser(tenantId, user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody LoginRequest loginRequest) throws NotFoundException {
        return new ResponseEntity<User>(userService.loginUser(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()), HttpStatus.OK);
    }


    @PostMapping("/{tenantId}/emailRecover")
    public ResponseEntity emailRecover(@PathVariable ObjectId tenantId, @RequestBody EmailRecoveryRequest request) throws NotFoundException {
        userService.sendOtpToUserEmail(tenantId, request.getEmail(), OtpPurpose.PASSWORD_RECOVERY);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/{tenantId}/verifyEmail")
    public ResponseEntity verifyEmail(@PathVariable ObjectId tenantId, @RequestBody EmailDTO emailDTO) throws NotFoundException {
        userService.sendOtpToUserEmail(tenantId, emailDTO.getEmail(), OtpPurpose.REGISTRATION);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/{tenantId}/verifyOtp")
    public ResponseEntity verifyOtp(@PathVariable ObjectId tenantId, @RequestBody OtpVerificationRequest request) throws NotFoundException {
        userService.verifyOtp(tenantId, request.getUserEmail(), request.getOtp());
        return new ResponseEntity(HttpStatus.OK);
    }


    @PutMapping("/{tenantId}/resetPassword")
    public ResponseEntity<String> resetPassword(@PathVariable ObjectId tenantId, @RequestBody ResetPasswordRequest resetRequest) {
        try {
            userService.resetPassword(tenantId, resetRequest.getUserEmail(), resetRequest.getSubmittedOtp(), resetRequest.getNewPassword());
            return new ResponseEntity<>("Password reset successfully", HttpStatus.CREATED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidOtpException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED); // or another appropriate status code
        } catch (WeakPasswordException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @PutMapping("/{tenantId}/update_{userId}")
    public ResponseEntity updateUser(@PathVariable ObjectId tenantId, @PathVariable String userId, @RequestBody User newUser) throws NotFoundException {
        userService.updateUser(tenantId, userId, newUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{tenantId}/delete_{userId}")
    public ResponseEntity deleteUser(@PathVariable ObjectId tenantId, @PathVariable String userId) throws NotFoundException {
        userService.deleteUserByUserId(tenantId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{tenantId}/assignrole_{userId}")
    public ResponseEntity assignRoles(@PathVariable ObjectId tenantId, @PathVariable String userId, @RequestBody List<String> roleIds) throws NotFoundException {
        userService.assignRolesByRoleId(tenantId, userId, roleIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
