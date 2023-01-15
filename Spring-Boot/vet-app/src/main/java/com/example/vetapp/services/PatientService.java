package com.example.vetapp.services;

import com.example.vetapp.config.CommonProcess;
import com.example.vetapp.exceptions.NotFoundException;
import com.example.vetapp.models.DataConverter;
import com.example.vetapp.models.documents.Patient;
import com.example.vetapp.models.request.PatientRequestObject;
import com.example.vetapp.models.response.PatientResponseObject;
import com.example.vetapp.models.response.ProfileResponseObject;
import com.example.vetapp.models.response.ResponseObject;
import com.example.vetapp.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;

@Service
public class PatientService {

    @Autowired
    private PatientRepository repository;

    public Patient findByIdAndVetId(String id, String veterinaryId) {
        return repository.findByIdAndVeterinaryId(id, veterinaryId).orElseThrow(() -> new NotFoundException("Patient not found"));
    }

    public ResponseEntity<ResponseObject> processToCreatePatient(PatientRequestObject data, BindingResult result, HttpServletRequest request) {
        ProfileResponseObject profile = (ProfileResponseObject) request.getAttribute("veterinary");

        ResponseEntity<ResponseObject> responseEntity = CommonProcess.validBindingResult(data, result);
        if(responseEntity != null) {
            return responseEntity;
        }

        Patient patient = DataConverter.patientRequestToPatientDocument(data, profile.getId());

        Patient patientSaved = repository.save(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(patientSaved, "Created successfully", true));
    }

    public ResponseEntity<ResponseObject> processToUpdatePatient(String id, PatientRequestObject data, BindingResult result, HttpServletRequest request) {
        ProfileResponseObject profile = (ProfileResponseObject) request.getAttribute("veterinary");
        ResponseEntity<ResponseObject> responseEntity = CommonProcess.validBindingResult(data, result);
        if(responseEntity != null) {
            return responseEntity;
        }

        try {
            Patient patient = findByIdAndVetId(id, profile.getId());
            boolean isUpdate = false;

            if(!patient.getName().equals(data.getName())) {
                patient.setName(data.getName());
                isUpdate = true;
            }

            if(!patient.getOwner().equals(data.getOwner())) {
                patient.setOwner(data.getOwner());
                isUpdate = true;
            }

            if(!patient.getEmail().equals(data.getEmail())) {
                patient.setEmail(data.getEmail());
                isUpdate = true;
            }

            if(!patient.getPhone().equals(data.getPhone())) {
                patient.setPhone(data.getPhone());
                isUpdate = true;
            }

            if(!patient.getSymptoms().equals(data.getSymptoms())) {
                patient.setSymptoms(data.getSymptoms());
                isUpdate = true;
            }

            if(!patient.getDate().equals(data.getDate())) {
                patient.setDate(data.getDate());
                isUpdate = true;
            }

            if(!isUpdate) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(patient, "Updated successfully", true));
            }

            patient.setUpdatedAt(new Date());
            Patient patientSaved = repository.save(patient);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(patientSaved, "Updated successfully", true));

        } catch (NotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    public ResponseEntity<ResponseObject> processToDeletePatient(String id, HttpServletRequest request) {
        ProfileResponseObject profile = (ProfileResponseObject) request.getAttribute("veterinary");
        try {
            Patient patient = findByIdAndVetId(id, profile.getId());
            repository.delete(patient);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Delete patient successfully"));

        } catch (NotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    public ResponseEntity<ResponseObject> getPatients(HttpServletRequest request) {
        ProfileResponseObject profile = (ProfileResponseObject) request.getAttribute("veterinary");
        ArrayList<PatientResponseObject> patients = repository.findAllByVeterinaryId(profile.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(patients, "Get patients successfully", true));
    }

    public ResponseEntity<ResponseObject> getPatient(String id ,HttpServletRequest request) {
        ProfileResponseObject profile = (ProfileResponseObject) request.getAttribute("veterinary");
        try {
            Patient patient = findByIdAndVetId(id, profile.getId());

            PatientResponseObject responseObject = DataConverter.patientDocumentToPatientResponse(patient);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(responseObject, "Get patient successfully", true));

        } catch (NotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }
}
