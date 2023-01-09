package com.example.uptask.service.interfaces;

import com.example.uptask.model.dto.*;
import com.example.uptask.model.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

public interface IUserService {

    public User findById(Long userId);

    public User findByEmail(String email);

    public User findByUser(String user);

    public ResponseEntity<ResponseObject> processToCreateUser(User user, BindingResult result);

    public ResponseEntity<ResponseObject> confirmUser(String token, Long userId);

    public ResponseEntity<ResponseObject> sendMailToRecover(DataRecoverAccount email, BindingResult result);

    public ResponseEntity<ResponseObject> validateToken(String token, Long userId);

    public ResponseEntity<ResponseObject> recoverAccountChangePassword(String token, Long userId, DataRecoverAccountCP newPassword, BindingResult result);

    public ResponseEntity<ResponseObject> authenticate(DataAuthRequest data, BindingResult result);

    public ResponseEntity<ResponseObject> getUserProfile(HttpServletRequest request);

    public ResponseEntity<ResponseObject> editProfile(HttpServletRequest request, DataEditUser data, BindingResult result);

    public ResponseEntity<ResponseObject> editProfileChangePassword(HttpServletRequest request, DataEditUserCP data, BindingResult result);

}