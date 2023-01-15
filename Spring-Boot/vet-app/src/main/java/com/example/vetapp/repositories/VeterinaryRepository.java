package com.example.vetapp.repositories;

import com.example.vetapp.models.documents.Veterinary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VeterinaryRepository extends MongoRepository<Veterinary, String> {

    public Optional<Veterinary> findByEmail(String email);

    public Optional<Veterinary> findByToken(String token);
}
