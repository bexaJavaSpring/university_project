package uz.java.spring_boot_application.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uz.java.spring_boot_application.exception.ValidationException;
import uz.java.spring_boot_application.security.CustomUserDetails;

import java.util.Objects;

@Component
public class UserSession {

    public CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails details = (Objects.isNull(authentication) || isAnonymous(authentication)) ? null : (CustomUserDetails) authentication.getPrincipal();
        if (details == null)
            throw new ValidationException("user.not.found");
        return details;
    }

    private static boolean isAnonymous(Authentication authentication) {
        return authentication.getPrincipal().equals("anonymousUser");
    }

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (!Objects.isNull(authentication) && !isAnonymous(authentication));
    }

}
