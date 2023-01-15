package com.example.vetapp.repositories;

import com.example.vetapp.models.documents.Patient;
import com.example.vetapp.models.response.PatientResponseObject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {
    ArrayList<PatientResponseObject> findAllByVeterinaryId(String veterinaryId);
    Optional<Patient> findByIdAndVeterinaryId(String id, String veterinaryId);
}
