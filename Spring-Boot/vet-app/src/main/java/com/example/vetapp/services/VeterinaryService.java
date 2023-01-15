package com.example.vetapp.services;

import com.example.vetapp.config.CommonProcess;
import com.example.vetapp.models.enums.EmailSubject;
import com.example.vetapp.exceptions.ApiException;
import com.example.vetapp.exceptions.MailerException;
import com.example.vetapp.exceptions.NotFoundException;
import com.example.vetapp.models.documents.Veterinary;
import com.example.vetapp.models.request.AuthRequestObject;
import com.example.vetapp.models.request.ProfileRequestObject;
import com.example.vetapp.models.request.RecoverRequestObject;
import com.example.vetapp.models.request.ResetRequestObject;
import com.example.vetapp.models.response.AuthResponseObject;
import com.example.vetapp.models.response.ProfileResponseObject;
import com.example.vetapp.models.response.ResponseObject;
import com.example.vetapp.repositories.VeterinaryRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class VeterinaryService {

    @Value("${secret.key}")
    private String secretKey;
    @Autowired
    private VeterinaryRepository repository;

    @Autowired
    private MailService mailService;

    public Veterinary findByEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    public Veterinary findById(String id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Veterinary not found with id: " + id));
    }

    public ResponseEntity<ResponseObject> processToCreate(Veterinary data, BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        ResponseEntity<ResponseObject> responseEntity = CommonProcess.validBindingResult(data, result);
        if(responseEntity != null) {
            return responseEntity;
        }

        try {
            if(findByEmail(data.getEmail()) != null) {
                errors.put("email", "The email is already registered");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(data, errors, "Failed Validation"));
            }

            data.setConfirm(false);
            data.setToken(UUID.randomUUID().toString());
            data.setPassword(new BCryptPasswordEncoder().encode(data.getPassword()));
            data.setCreatedAt(new Date());
            data.setUpdatedAt(new Date());

            Veterinary veterinarySaved = repository.save(data);

            mailService.sendEmail(veterinarySaved.getFirstName() + " " + veterinarySaved.getLastName(), veterinarySaved.getEmail(), veterinarySaved.getToken(), EmailSubject.REGISTER);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(veterinarySaved, "Created successfully", true));

        } catch (MailerException exception) {
            throw new MailerException(exception.getMessage());

        } catch (Exception exception) {
            throw new ApiException(exception.getMessage());
        }
    }

    public ResponseEntity<ResponseObject> processToConfirm(String token) {
        try {
            Veterinary veterinaryToConfirm = repository.findByToken(token).orElseThrow(() -> new NotFoundException("Not found the token " + token));

            veterinaryToConfirm.setConfirm(true);
            veterinaryToConfirm.setToken(null);
            veterinaryToConfirm.setUpdatedAt(new Date());

            repository.save(veterinaryToConfirm);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Confirmed successfully"));

        } catch (NotFoundException exception) {
            throw new NotFoundException(exception.getMessage());

        }  catch (Exception exception) {
            throw new ApiException(exception.getMessage());
        }
    }

    public ResponseEntity<ResponseObject> processToRecover(RecoverRequestObject data, BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        ResponseEntity<ResponseObject> responseEntity = CommonProcess.validBindingResult(data, result);
        if(responseEntity != null) {
            return responseEntity;
        }

        try {
            Veterinary existVeterinary = findByEmail(data.getEmail());

            if(existVeterinary == null || !existVeterinary.getConfirm()) {
                errors.put("email", "The email is not registered or confirmed");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(errors, "Failed Validation", false));
            }

            existVeterinary.setToken(UUID.randomUUID().toString());
            existVeterinary.setUpdatedAt(new Date());
            repository.save(existVeterinary);

            mailService.sendEmail(existVeterinary.getFirstName() + " " + existVeterinary.getLastName(), existVeterinary.getEmail(), existVeterinary.getToken(), EmailSubject.RECOVER);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Request successfully"));

        } catch (MailerException exception) {
            throw new MailerException(exception.getMessage());

        } catch (NotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }

        catch (Exception exception) {
            throw new ApiException(exception.getMessage());
        }
    }

    public ResponseEntity<ResponseObject> validateToken(String token) {
        try {
            Veterinary veterinary = repository.findByToken(token).orElseThrow(() -> new NotFoundException("Not found the token " + token));

            if(!veterinary.getConfirm()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("The email is not confirmed"));
            }

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Request successfully"));

        } catch (NotFoundException exception) {
            throw new NotFoundException(exception.getMessage());

        } catch (Exception exception) {
            throw new ApiException(exception.getMessage());
        }
    }

    public ResponseEntity<ResponseObject> processToResetPassword(String token, ResetRequestObject data, BindingResult result) {

        try {
            Veterinary veterinaryToUpdate = repository.findByToken(token).orElseThrow(() -> new NotFoundException("Not found the token " + token));

            ResponseEntity<ResponseObject> responseEntity = CommonProcess.validBindingResult(data, result);
            if(responseEntity != null) {
                return responseEntity;
            }

            veterinaryToUpdate.setPassword(new BCryptPasswordEncoder().encode(data.getPassword()));
            veterinaryToUpdate.setToken(null);
            veterinaryToUpdate.setUpdatedAt(new Date());

            repository.save(veterinaryToUpdate);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Reset password successfully"));

        } catch (NotFoundException exception) {
            throw new NotFoundException(exception.getMessage());

        } catch (Exception exception) {
            throw new ApiException(exception.getMessage());
        }
    }

    public ResponseEntity<ResponseObject> processToAuthenticate(AuthRequestObject data, BindingResult result) {
        Map<String, String> error = new HashMap<>();
        ResponseEntity<ResponseObject> responseEntity = CommonProcess.validBindingResult(data, result);
        if(responseEntity != null) {
            return responseEntity;
        }

        try {
            Veterinary veterinary = findByEmail(data.getEmail());
            if(veterinary == null || !veterinary.getConfirm()) {
                error.put("email", "The email is not registered or is not confirmed");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(error, "Failed validation", false));
            }

            if(!BCrypt.checkpw(data.getPassword(), veterinary.getPassword())) {
                error.put("password", "The password is incorrect");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(error, "Failed validation", false));
            }

            Map<String, Object> jwtMapper = new HashMap<>();
            jwtMapper.put("_id", veterinary.getId());
            String jws = Jwts.builder()
                    .setHeaderParam("typ", "JWT")
                    .setClaims(jwtMapper)
                    .setIssuedAt(new Date())
                    .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                    .compact();

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(new AuthResponseObject(true, jws), "success", true));

        } catch (Exception exception) {
            throw new ApiException(exception.getMessage());
        }
    }

    public ResponseEntity<ResponseObject> processToGetProfile(HttpServletRequest request) {
        ProfileResponseObject profile = (ProfileResponseObject) request.getAttribute("veterinary");
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(profile, "Get profile successfully", true));
    }

    public ResponseEntity<ResponseObject> processToUpdateProfile(ProfileRequestObject data, BindingResult result, HttpServletRequest request) {

        boolean isUpdate = false;
        ProfileResponseObject profile = (ProfileResponseObject) request.getAttribute("veterinary");

        Map<String, String> errors = new HashMap<>();
        ResponseEntity<ResponseObject> responseEntity = CommonProcess.validBindingResult(data, result);
        if(responseEntity != null) {
            return responseEntity;
        }

        try {
            Veterinary veterinary = findById(profile.getId());

            if(!veterinary.getEmail().equals(data.getEmail()) && findByEmail(data.getEmail()) != null) {
                errors.put("email", "The email is already registered");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(data, errors, "Failed validation"));
            }

            if(!veterinary.getFirstName().equals(data.getFirstName())) {
                veterinary.setFirstName(data.getFirstName());
                isUpdate = true;
            }

            if(!veterinary.getLastName().equals(data.getLastName())) {
                veterinary.setLastName(data.getLastName());
                isUpdate = true;
            }

            if(!veterinary.getEmail().equals(data.getEmail())) {
                veterinary.setEmail(data.getEmail());
                isUpdate = true;
            }

            if(!isUpdate) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(data, "Profile updated successfully", true));
            }

            veterinary.setUpdatedAt(new Date());
            Veterinary veterinarySaved = repository.save(veterinary);
            ProfileResponseObject newProfile = new ProfileResponseObject(veterinarySaved);
            request.setAttribute("veterinary", newProfile);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(newProfile, "Profile updated successfully", true));

        } catch (NotFoundException exception) {
            throw new NotFoundException("Veterinary not found");

        } catch (Exception exception) {
            throw new ApiException(exception.getMessage());
        }
    }
}
