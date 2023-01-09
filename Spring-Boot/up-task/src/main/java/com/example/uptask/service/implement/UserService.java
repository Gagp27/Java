package com.example.uptask.service.implement;

import com.example.uptask.exceptions.EntityNotFoundException;
import com.example.uptask.model.dto.*;
import com.example.uptask.repository.UserRepository;
import com.example.uptask.service.interfaces.IUserService;
import com.example.uptask.model.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class UserService implements IUserService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User findByUser(String user) {
        return userRepository.findByUserName(user).orElse(null);
    }

    @Override
    public ResponseEntity<ResponseObject> processToCreateUser(User user, BindingResult result) {
        ResponseObject response = new ResponseObject();
        Map<String, String> errors = new HashMap<>();

        if(result.hasErrors()) {
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            response.setErrors(errors);
            response.setData(user);
            response.setMessage("Failed validation");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            if(findByUser(user.getUserName()) != null) {
                errors.put("userName", "The user name is already registered");
                response.setErrors(errors);
                response.setData(user);
                response.setMessage("Failed validation");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if(findByEmail(user.getEmail()) != null) {
                errors.put("email", "The email is already registered");
                response.setErrors(errors);
                response.setData(user);
                response.setMessage("Failed validation");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            user.setConfirm(false);
            user.setToken(UUID.randomUUID().toString());
            user.setCreatedAt(new Date());
            user.setUpdatedAt(new Date());
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

            User userSaved = userRepository.save(user);
            mailService.sendRegisterEmail(userSaved.getUserId(), userSaved.getUserName(), userSaved.getEmail(), userSaved.getToken());

            response.setMessage("User created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception exception) {
            response.setMessage(exception.getMessage());
            response.setErrors(exception.getCause());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<ResponseObject> confirmUser(String token, Long userId) {
        ResponseObject response = new ResponseObject();

        try {
            User userToConfirm = findById(userId);

            if(userToConfirm.getToken() == null || !userToConfirm.getToken().equals(token)) {
                response.setErrors("Invalid token");
                response.setMessage("The token is not valid or expired");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            userToConfirm.setConfirm(true);
            userToConfirm.setToken(null);
            userToConfirm.setUpdatedAt(new Date());
            userRepository.save(userToConfirm);

            response.setMessage("User confirmed successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (EntityNotFoundException exception) {
            throw new EntityNotFoundException("Not found the user with id: " + userId);

        } catch (Exception exception) {
            response.setErrors("Internal server error");
            response.setMessage(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<ResponseObject> sendMailToRecover(DataRecoverAccount email, BindingResult result) {
        ResponseObject response = new ResponseObject();
        Map<String, String> errors = new HashMap<>();

        if(result.hasErrors()) {
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            response.setData(email);
            response.setErrors(errors);
            response.setMessage("Failed Validation");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            User existEmail = findByEmail(email.getEmail());

            if(existEmail == null || !existEmail.isConfirm()) {
                errors.put("email", "The email isn't register or not confirmed");
                response.setData(email);
                response.setErrors(errors);
                response.setMessage("Failed validation");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            //TODO send Email
            existEmail.setToken(UUID.randomUUID().toString());
            existEmail.setUpdatedAt(new Date());
            userRepository.save(existEmail);
            mailService.sendRecoverEmail(existEmail.getUserId(), existEmail.getUserName(), existEmail.getEmail(), existEmail.getToken());
            response.setMessage("An email has been sent to recover your account");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception exception) {
            response.setErrors("Internal server error");
            response.setMessage(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<ResponseObject> validateToken(String token, Long userId) throws EntityNotFoundException {
        ResponseObject response = new ResponseObject();

        try {
            User userToValidate = findById(userId);

            if(!userToValidate.isConfirm() || userToValidate.getToken() == null || !userToValidate.getToken().equals(token)) {
                response.setErrors("Invalid token");
                response.setMessage("The token is not valid or expired");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Map<String, Boolean> data = new HashMap<>();
            data.put("isValid", true);
            response.setData(data);
            response.setMessage("Token valid");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (EntityNotFoundException exception) {
            throw new EntityNotFoundException("Not found the user with id: " + userId);

        } catch (Exception exception) {
            response.setErrors("Internal server error");
            response.setMessage(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<ResponseObject> recoverAccountChangePassword(String token, Long userId, DataRecoverAccountCP newPassword, BindingResult result) {
        ResponseObject response = new ResponseObject();
        Map<String, String> errors = new HashMap<>();

        if(result.hasErrors()) {
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            response.setErrors(errors);
            response.setMessage("Failed Validation");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            User userToValidate = findById(userId);

            if(!userToValidate.isConfirm() || userToValidate.getToken() == null || !userToValidate.getToken().equals(token)) {
                response.setErrors("Invalid token");
                response.setMessage("The token is not valid or expired");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            userToValidate.setPassword(new BCryptPasswordEncoder().encode(newPassword.getPassword()));
            userToValidate.setToken(null);
            userToValidate.setUpdatedAt(new Date());
            userRepository.save(userToValidate);

            response.setMessage("Recover account successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (EntityNotFoundException exception) {
            throw new EntityNotFoundException("Not found the user with id: " + userId);

        } catch (Exception exception) {
            response.setErrors("Internal server error");
            response.setMessage(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<ResponseObject> authenticate(DataAuthRequest data, BindingResult result) {
        ResponseObject response = new ResponseObject();
        Map<String, String> errors = new HashMap<>();

        if(result.hasErrors()) {
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            response.setData(data);
            response.setErrors(errors);
            response.setMessage("Failed Validation");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            User userToAuth = findByEmail(data.getEmail());

            if(userToAuth == null || !userToAuth.isConfirm()) {
                errors.put("email", "The email isn't register or not confirmed");
                response.setData(data);
                response.setErrors(errors);
                response.setMessage("Failed Validation");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if(!new BCryptPasswordEncoder().matches(data.getPassword(), userToAuth.getPassword())) {
                errors.put("password", "Invalid password");
                response.setData(data);
                response.setErrors(errors);
                response.setMessage("Failed Validation");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Map<String, Object> jwtMapper = new HashMap<>();
            Date today = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.add(Calendar.DATE, 1);
            Date expiration = calendar.getTime();

            jwtMapper.put("userId", userToAuth.getUserId());
            jwtMapper.put("email", userToAuth.getEmail());
            String jws = Jwts.builder()
                    .setHeaderParam("typ", "JWT")
                    .setClaims(jwtMapper)
                    .setIssuedAt(today)
                    .setExpiration(expiration)
                    .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                    .compact();
            response.setData(new DataAuthResponse(true, jws));
            response.setMessage("Authenticate successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception exception) {
            response.setErrors("Internal server error");
            response.setMessage(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<ResponseObject> getUserProfile(HttpServletRequest request) {
        DataUserProfile userProfile = (DataUserProfile) request.getAttribute("user");
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(userProfile, null, "Get profile success"));
    }

    @Override
    public ResponseEntity<ResponseObject> editProfile(HttpServletRequest request, DataEditUser data, BindingResult result) {
        DataUserProfile userProfile = (DataUserProfile) request.getAttribute("user");
        ResponseObject response = new ResponseObject();
        Map<String, String> errors = new HashMap<>();
        boolean isUpdate = false;

        if(result.hasErrors()) {
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            response.setData(data);
            response.setErrors(errors);
            response.setMessage("Failed Validation");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            User userForUpdate = findById(userProfile.getUserId());

            if(!userForUpdate.getUserName().equals(data.getUserName()) && findByUser(data.getUserName()) != null) {
                errors.put("userName", "The user name is already registered");
                response.setErrors(errors);
                response.setData(data);
                response.setMessage("Failed validation");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if(!userForUpdate.getEmail().equals(data.getEmail())  && findByEmail(data.getEmail()) != null) {
                errors.put("email", "The email is already registered");
                response.setErrors(errors);
                response.setData(data);
                response.setMessage("Failed validation");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }


            if(!userForUpdate.getFirstName().equals(data.getFirstName())) {
                userForUpdate.setFirstName(data.getFirstName());
                isUpdate = true;
            }

            if(!userForUpdate.getLastName().equals(data.getLastName())) {
                userForUpdate.setLastName(data.getLastName());
                isUpdate = true;
            }

            if(!userForUpdate.getUserName().equals(data.getUserName())) {
                userForUpdate.setUserName(data.getUserName());
                isUpdate = true;
            }

            if(!userForUpdate.getEmail().equals(data.getEmail())) {
                userForUpdate.setEmail(data.getEmail());
                isUpdate = true;
            }

            if(!isUpdate) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(userProfile, null, "Updated profile success"));
            }

            userForUpdate.setUpdatedAt(new Date());
            User userSaved = userRepository.save(userForUpdate);
            userProfile.setUserName(userSaved.getUserName());
            userProfile.setFirstName(userSaved.getFirstName());
            userProfile.setLastName(userSaved.getLastName());
            userProfile.setEmail(userSaved.getEmail());
            request.setAttribute("user", userProfile);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(userProfile, null, "Updated profile success"));

        } catch (EntityNotFoundException exception) {
            throw new EntityNotFoundException("Not found the user with id: " + userProfile.getUserId());

        }catch (Exception exception) {
            response.setErrors("Internal server error");
            response.setMessage(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @Override
    public ResponseEntity<ResponseObject> editProfileChangePassword(HttpServletRequest request, DataEditUserCP data, BindingResult result) {
        DataUserProfile userProfile = (DataUserProfile) request.getAttribute("user");
        ResponseObject response = new ResponseObject();
        Map<String, String> errors = new HashMap<>();

        if(result.hasErrors()) {
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            response.setData(data);
            response.setErrors(errors);
            response.setMessage("Failed Validation");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            User userForUpdate = findById(userProfile.getUserId());
            if(!new BCryptPasswordEncoder().matches(data.getCurrentPassword(), userForUpdate.getPassword())) {
                errors.put("currentPassword", "Incorrect current password");
                response.setData(data);
                response.setErrors(errors);
                response.setMessage("Failed Validation");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if(new BCryptPasswordEncoder().matches(data.getNewPassword(), userForUpdate.getPassword())) {
                errors.put("newPassword", "The new password cannot be the same as the current one");
                response.setData(data);
                response.setErrors(errors);
                response.setMessage("Failed Validation");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            userForUpdate.setPassword(new BCryptPasswordEncoder().encode(data.getNewPassword()));
            userForUpdate.setUpdatedAt(new Date());
            userRepository.save(userForUpdate);

            response.setData(null);
            response.setErrors(null);
            response.setMessage("Change password successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (EntityNotFoundException exception) {
            throw new EntityNotFoundException("Not found the user with id: " + userProfile.getUserId());
        } catch (Exception exception) {
            response.setErrors("Internal server error");
            response.setMessage(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}