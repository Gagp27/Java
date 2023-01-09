package com.example.uptask.middleware;

import com.example.uptask.exceptions.ApiException;
import com.example.uptask.exceptions.InvalidJwtException;
import com.example.uptask.model.dto.DataJwt;
import com.example.uptask.model.dto.DataUserProfile;
import com.example.uptask.model.entity.User;
import com.example.uptask.service.implement.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component("authMiddleware")
public class AuthMiddleware implements HandlerInterceptor {

    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if(request.getHeader("Authorization") == null || !request.getHeader("Authorization").startsWith("Bearer")) {
            throw new InvalidJwtException("Missing jsonwebtoken", "No valid jsonwebtoken found");
        }

        String[] jwt = request.getHeader("Authorization").split(" ");
        String[] chunks = jwt[1].split("\\.");

        try {
            Base64.Decoder decoder = Base64.getUrlDecoder();

            String header = new String(decoder.decode(chunks[0]));
            String payload = new String(decoder.decode(chunks[1]));

            SignatureAlgorithm sa = SignatureAlgorithm.HS256;
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), sa.getJcaName());

            String tokenWithoutSignature = chunks[0] + "." + chunks[1];
            String signature = chunks[2];

            DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);

            if(!validator.isValid(tokenWithoutSignature, signature)) {
                throw new InvalidJwtException("Jsonwebtoken validation", "Could not verify JWT token integrity!");
            }

            DataJwt objectMapper = new ObjectMapper().readValue(payload, DataJwt.class);

            User user = userService.findById(objectMapper.getUserId());
            DataUserProfile userProfile = new DataUserProfile();
            userProfile.setUserId(user.getUserId());
            userProfile.setUserName(user.getUserName());
            userProfile.setFirstName(user.getFirstName());
            userProfile.setLastName(user.getLastName());
            userProfile.setEmail(user.getEmail());

            request.setAttribute("user", userProfile);

        } catch (InvalidJwtException exception) {
            throw new InvalidJwtException("Jsonwebtoken validation", "Could not verify JWT token integrity!");

        } catch (JsonProcessingException exception) {
            throw new ApiException(exception.getMessage());
        }

        return true;
    }
}