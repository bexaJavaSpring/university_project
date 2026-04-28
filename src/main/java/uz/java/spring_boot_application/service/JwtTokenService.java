package uz.java.spring_boot_application.service;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.java.spring_boot_application.exception.JWTTokenExpiredException;
import uz.java.spring_boot_application.exception.JWTVerificationException;

import java.net.MalformedURLException;
import java.net.URI;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
@Service
public class JwtTokenService {

    private final JwkProvider jwkProvider;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuUrl;

    public JwtTokenService(@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwkSetUri) throws MalformedURLException {
        this.jwkProvider = new UrlJwkProvider(URI.create(jwkSetUri).toURL());
    }

    public DecodedJWT validate(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            String kid = jwt.getKeyId();
            Jwk jwk = jwkProvider.get(kid);
            RSAPublicKey publicKey = (RSAPublicKey) jwk.getPublicKey();

            Algorithm algorithm = Algorithm.RSA256(publicKey, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuUrl)
                    .build();
            DecodedJWT verified = verifier.verify(token);
            Date expiresAt = verified.getExpiresAt();
            if (expiresAt == null || expiresAt.before(new Date())) {
                throw new JWTVerificationException("token.expired");
            }
            return verified;
        } catch (JwkException | TokenExpiredException e) {
            throw new JWTTokenExpiredException(e.getMessage());
        }
    }
}
