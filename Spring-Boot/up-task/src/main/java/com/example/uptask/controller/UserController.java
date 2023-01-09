package com.example.uptask.controller;

import com.example.uptask.model.dto.*;
import com.example.uptask.model.entity.User;
import com.example.uptask.service.implement.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    ResponseEntity<ResponseObject> createAccount(@Valid @RequestBody User user, BindingResult result) {
        return userService.processToCreateUser(user, result);
    }

    @GetMapping("/confirm/{token}/{userId}")
    ResponseEntity<ResponseObject> confirmAccount(@PathVariable("token") String token, @PathVariable("userId") Long userId) {
        return userService.confirmUser(token, userId);
    }

    @PostMapping("/recover-account")
    ResponseEntity<ResponseObject> recoverAccount(@Valid @RequestBody DataRecoverAccount email, BindingResult result) {
        return userService.sendMailToRecover(email, result);
    }

    @GetMapping("/recover-account/{token}/{userId}")
    ResponseEntity<ResponseObject> recoverAccount(@PathVariable("token") String token, @PathVariable("userId") Long userId) {
        return userService.validateToken(token, userId);
    }

    @PostMapping("/recover-account/{token}/{userId}")
    ResponseEntity<ResponseObject> recoverAccount(@PathVariable("token") String token, @PathVariable("userId") Long userId, @Valid @RequestBody DataRecoverAccountCP password, BindingResult result) {
        return userService.recoverAccountChangePassword(token, userId, password, result);
    }

    @PostMapping("")
    ResponseEntity<ResponseObject> authenticate(@Valid @RequestBody DataAuthRequest data, BindingResult result) {
        return userService.authenticate(data, result);
    }

    @GetMapping("/profile")
    ResponseEntity<ResponseObject> profile(HttpServletRequest request) {
        return userService.getUserProfile(request);
    }

    @PutMapping("/profile/edit")
    ResponseEntity<ResponseObject> profileEdit(HttpServletRequest request, @Valid @RequestBody DataEditUser data, BindingResult result) {
        return userService.editProfile(request, data, result);
    }

    @PutMapping("/profile/change-password")
    ResponseEntity<ResponseObject> profileChangePassword(HttpServletRequest request, @Valid @RequestBody DataEditUserCP data, BindingResult result) {
        return userService.editProfileChangePassword(request, data, result);
    }
}