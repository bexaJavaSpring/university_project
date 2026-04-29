package uz.java.spring_boot_application.service;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.java.spring_boot_application.util.JwtUtil;

import java.util.HashMap;

@Service
public class JwtTokenService {

    private final JwtUtil jwtUtil;

    @Value("${jwt.token.secret}")
    private String tokenSecret;

    public JwtTokenService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public Boolean isValid(String token) {
        return jwtUtil.isTokenValid(token, getTokenSecret());
    }

    public String generateToken(@NonNull String subject) {
        return jwtUtil.jwt(new HashMap<>(), subject, getTokenSecret());
    }

    public String subject(String token) {
        return jwtUtil.getSubject(token, getTokenSecret());
    }

    private String getTokenSecret() {
        return tokenSecret;
    }
}
