package com.example.vetapp.controllers;

import com.example.vetapp.models.request.PatientRequestObject;
import com.example.vetapp.models.response.ResponseObject;
import com.example.vetapp.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;


    @GetMapping("/")
    public ResponseEntity<ResponseObject> getPatients(HttpServletRequest request) {
        return patientService.getPatients(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getPatient(@PathVariable("id") String id, HttpServletRequest request) {
        return patientService.getPatient(id, request);
    }

    @PostMapping("/")
    public ResponseEntity<ResponseObject> createPatient(@Valid @RequestBody PatientRequestObject data, BindingResult result, HttpServletRequest request) {
        return patientService.processToCreatePatient(data, result, request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updatePatient(@PathVariable("id") String id, @Valid @RequestBody PatientRequestObject data, BindingResult result, HttpServletRequest request) {
        return patientService.processToUpdatePatient(id, data, result, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deletePatient(@PathVariable("id") String id, HttpServletRequest request) {
        return patientService.processToDeletePatient(id, request);
    }
}
