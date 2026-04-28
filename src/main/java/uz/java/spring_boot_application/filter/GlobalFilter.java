package uz.java.spring_boot_application.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.LocaleResolver;
import uz.java.spring_boot_application.exception.GenericRuntimeException;
import uz.java.spring_boot_application.security.CustomUserDetails;
import uz.java.spring_boot_application.service.CustomUserDetailService;
import uz.java.spring_boot_application.service.JwtTokenService;
import uz.java.spring_boot_application.util.ApiConstants;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import static uz.java.spring_boot_application.config.SecurityConfig.AUTH_WHITELIST;

@Component
@Slf4j
@RequiredArgsConstructor
public class GlobalFilter extends OncePerRequestFilter {

//    private static final Logger logger = Logger.getLogger(GlobalFilter.class.getName());
    private final JwtTokenService jwtTokenService;
    private final CustomUserDetailService userDetailsService;
    private final LocaleResolver localeResolver;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
    // HandlerExceptionResolver class agar global filter ichida exception tashlasa
    // faqatgina GloabalExceptionHandler ni yetarli bolmaydi va shu class yordam beradi

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        String requestUri = request.getRequestURI();
        log.info("getting request URI: " + requestUri);
        if (!isOpenPath(requestUri)) {
            try {
                String token = getTokenFromRequest(request);
                DecodedJWT verified = jwtTokenService.validate(token);
                String username = verified.getClaim("preferred_username").asString();
                CustomUserDetails customUserDetails = userDetailsService.loadUserByUsername(username);
                authenticate(customUserDetails);
                log.info(String.format("Authenticated user: %s", username));
            } catch (GenericRuntimeException e) {
                log.error("Global filter error", e);
                resolver.resolveException(request, response, null, e);
                return;
            }
        }
        setLang(request, response);
        filterChain.doFilter(request, response);
        long finish = System.currentTimeMillis();
        log.info("->->Request = [ {}?{} ] Elapsed time to proceed this request = {}", request.getRequestURI(),
                request.getQueryString() == null ? "" : request.getQueryString(), finish - start);
    }

    private void authenticate(CustomUserDetails customUserDetails) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(ApiConstants.HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.trim().substring(7);
        } else {
            throw new RuntimeException("token.is.null");
        }
    }

    private boolean isOpenPath(String currentPath) {
        for (String path : AUTH_WHITELIST) {
            if (currentPath.contains(path)) {
                return true;
            }
        }
        return false;
    }

    private void setLang(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(ApiConstants.LANG);
        localeResolver.setLocale(request, response, new Locale(Objects.requireNonNullElse(header, ApiConstants.DEFAULT_LANG)));
    }
}
