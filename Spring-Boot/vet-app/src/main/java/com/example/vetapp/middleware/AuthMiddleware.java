package com.example.vetapp.middleware;

import com.example.vetapp.exceptions.ApiException;
import com.example.vetapp.exceptions.JwtException;
import com.example.vetapp.exceptions.NotFoundException;
import com.example.vetapp.models.documents.Veterinary;
import com.example.vetapp.models.response.ProfileResponseObject;
import com.example.vetapp.services.VeterinaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
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

    @Value("${secret.key}")
    private String secretKey;

    @Autowired
    private VeterinaryService veterinaryService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getHeader("Authorization") == null || !request.getHeader("Authorization").startsWith("Bearer")) {
            throw new JwtException("Missing JWT or missing Authorization header");
        }

        String[] jwt = request.getHeader("Authorization").split(" ");
        String[] chunks = jwt[1].split("\\.");

        try {
            Base64.Decoder decoder = Base64.getUrlDecoder();

            String header = new String(decoder.decode(chunks[0]));
            String payload = new String(decoder.decode(chunks[1]));

            SignatureAlgorithm sa = SignatureAlgorithm.HS256;
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), sa.getJcaName());


            String tokenWithSignature = chunks[0] + "." + chunks[1];
            String signature = chunks[2];

            DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);

            if (!validator.isValid(tokenWithSignature, signature)) {
                throw new JwtException("Could not verify JWT token integrity!");
            }

            JWT objectMapper = new ObjectMapper().readValue(payload, JWT.class);

            Veterinary veterinary = veterinaryService.findById(objectMapper.get_id());
            ProfileResponseObject profile = new ProfileResponseObject(veterinary);
            request.setAttribute("veterinary", profile);
            return true;

        } catch(NotFoundException exception) {
            throw new NotFoundException(exception.getMessage());

        } catch(JwtException exception) {
            throw new JwtException("Could not verify JWT token integrity!");

        } catch (Exception exception) {
            throw new ApiException(exception.getMessage());
        }
    }
}
