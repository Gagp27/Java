package com.example.vetapp.controllers;

import com.example.vetapp.models.documents.Veterinary;
import com.example.vetapp.models.request.AuthRequestObject;
import com.example.vetapp.models.request.ProfileRequestObject;
import com.example.vetapp.models.request.RecoverRequestObject;
import com.example.vetapp.models.request.ResetRequestObject;
import com.example.vetapp.models.response.ProfileResponseObject;
import com.example.vetapp.models.response.ResponseObject;
import com.example.vetapp.services.VeterinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/veterinary")
public class VeterinaryController {

    @Autowired
    private VeterinaryService veterinaryService;

    @PostMapping("/register")
    ResponseEntity<ResponseObject> register(@Valid @RequestBody Veterinary data, BindingResult result) {
        return veterinaryService.processToCreate(data, result);
    }

    @GetMapping("/confirm/{token}")
    ResponseEntity<ResponseObject> confirm(@PathVariable String token) {
        return veterinaryService.processToConfirm(token);
    }

    @PostMapping("/recover-account")
    ResponseEntity<ResponseObject> recover(@Valid @RequestBody RecoverRequestObject data, BindingResult result) {
        return veterinaryService.processToRecover(data, result);
    }

    @GetMapping("/recover-account/{token}")
    ResponseEntity<ResponseObject> validToken(@PathVariable("token") String token) {
        return veterinaryService.validateToken(token);
    }

    @PostMapping("/recover-account/{token}")
    ResponseEntity<ResponseObject> resetPassword(@PathVariable("token") String token, @Valid @RequestBody ResetRequestObject data, BindingResult result) {
        return veterinaryService.processToResetPassword(token, data, result);
    }

    @PostMapping("/authenticate")
    ResponseEntity<ResponseObject> authenticate(@Valid @RequestBody AuthRequestObject data, BindingResult result) {
        return veterinaryService.processToAuthenticate(data, result);
    }

    @GetMapping("/profile")
    ResponseEntity<ResponseObject> profile(HttpServletRequest request) {
        return veterinaryService.processToGetProfile(request);
    }

    @PutMapping("/profile/edit")
    ResponseEntity<ResponseObject> editProfile(@Valid @RequestBody ProfileRequestObject data, BindingResult result, HttpServletRequest request) {
        return veterinaryService.processToUpdateProfile(data, result, request);
    }
}
