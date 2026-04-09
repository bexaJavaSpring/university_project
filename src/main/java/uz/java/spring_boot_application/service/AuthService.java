package uz.java.spring_boot_application.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import uz.java.spring_boot_application.config.CustomAuthenticationProvider;
import uz.java.spring_boot_application.dto.auth.LoginResponse;
import uz.java.spring_boot_application.entities.SessionUser;
import uz.java.spring_boot_application.entities.Status;
import uz.java.spring_boot_application.entities.User;
import uz.java.spring_boot_application.exception.GenericNotFoundException;
import uz.java.spring_boot_application.repository.SessionUserRepository;
import uz.java.spring_boot_application.repository.UserRepository;
import uz.java.spring_boot_application.security.CustomUserDetails;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final SessionUserRepository sessionUserRepository;
    private final JwtTokenService jwtTokenService;
    private final CustomAuthenticationProvider authenticationProvider;

    public AuthService(UserRepository userRepository, SessionUserRepository sessionUserRepository, JwtTokenService jwtTokenService, CustomAuthenticationProvider authenticationProvider) {
        this.userRepository = userRepository;
        this.sessionUserRepository = sessionUserRepository;
        this.jwtTokenService = jwtTokenService;
        this.authenticationProvider = authenticationProvider;
    }

    public LoginResponse login(String username, String password) {
        Authentication authenticate = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(
                username, password
        ));
        CustomUserDetails userDetails = (CustomUserDetails) authenticate.getPrincipal();
        User user = userRepository.findById(userDetails.getUserId()).orElse(null);
        if (user == null)
            throw new GenericNotFoundException(userDetails.getUserId().toString(), "user.not.found");

        SessionUser sessionUser = sessionUserRepository.findByUserId(user.getId());
        if (sessionUser != null) {
            String accessToken = jwtTokenService.generateToken(userDetails.getUsername());
            String refreshToken = jwtTokenService.generateToken(userDetails.getUsername());
            sessionUser.setAccessToken(accessToken);
            sessionUser.setRefreshToken(refreshToken);
            sessionUser.setStatus(Status.ACTIVE);
            sessionUserRepository.save(sessionUser);
            return LoginResponse.builder()
                    .accessToken(sessionUser.getAccessToken())
                    .refreshToken(sessionUser.getRefreshToken())
                    .build();
        }
        String accessToken = jwtTokenService.generateToken(userDetails.getUsername());
        String refreshToken = jwtTokenService.generateToken(userDetails.getUsername());
        sessionUserRepository.save(SessionUser.builder()
                .user(user)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .status(Status.ACTIVE)
                .build());
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
