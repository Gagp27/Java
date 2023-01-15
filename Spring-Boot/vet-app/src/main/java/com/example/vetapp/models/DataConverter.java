package com.example.vetapp.models;

import com.example.vetapp.models.documents.Patient;
import com.example.vetapp.models.request.PatientRequestObject;
import com.example.vetapp.models.response.PatientResponseObject;

public class DataConverter {

    public static Patient patientRequestToPatientDocument(PatientRequestObject data) {
        Patient patient = new Patient();
        patient.setName(data.getName());
        patient.setOwner(data.getOwner());
        patient.setEmail(data.getEmail());
        patient.setPhone(data.getPhone());
        patient.setDate(data.getDate());
        patient.setSymptoms(data.getSymptoms());
        return patient;
    }

    public static Patient patientRequestToPatientDocument(PatientRequestObject data, String veterinaryId) {
        Patient patient = patientRequestToPatientDocument(data);
        patient.setVeterinaryId(veterinaryId);
        return patient;
    }

    public static PatientResponseObject patientDocumentToPatientResponse(Patient data) {
        PatientResponseObject patientResponse = new PatientResponseObject();
        patientResponse.setName(data.getName());
        patientResponse.setOwner(data.getOwner());
        patientResponse.setEmail(data.getEmail());
        patientResponse.setPhone(data.getPhone());
        patientResponse.setDate(data.getDate());
        patientResponse.setSymptoms(data.getSymptoms());
        return patientResponse;
    }
}
