package uz.java.spring_boot_application.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import uz.java.spring_boot_application.filter.GlobalFilter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import uz.java.spring_boot_application.security.CustomUserDetails;
import uz.java.spring_boot_application.service.CustomUserDetailService;


import java.util.Collection;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final GlobalFilter globalFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final CustomUserDetailService customUserDetailService;


    public static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui",
            "/v3/**",
            "/v3",
            "/webjars/**",
            "/webjars",
            "/auth/login",
            "/files/upload",
            // vaqtincha Firebase uchun
            "/home",
            "/home/**",
            "/.well-known/appspecific/com.chrome.devtools.json",
            "/firebase-messaging-sw.js"
    };

    //     Basic authorization
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // vaqtincha Firebase uchun
                        .requestMatchers(
                                "/",
                                "/index",
                                "/firebase-messaging-sw.js",
                                "/favicon.ico",
                                "/*.js",
                                "/*.css",
                                "/**"
                        ).permitAll()
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2ResourceServer -> {
                    oauth2ResourceServer.jwt(jwt-> jwt.jwtAuthenticationConverter(customJwtAuthenticationConverter()));
                })
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(corsFilter(), ChannelProcessingFilter.class)
                .addFilterBefore(globalFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                        httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(authenticationEntryPoint));

        return http.build();
    }

    private Converter<Jwt, UsernamePasswordAuthenticationToken> customJwtAuthenticationConverter() {
        return jwt -> {
            String username = jwt.getClaim("preferred_username"); // Keycloak da jwt token ni ichidan user ni username ini olish
            CustomUserDetails user = customUserDetailService.loadUserByUsername(username);
            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
            return new UsernamePasswordAuthenticationToken(user, jwt, authorities);
        };
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:7777", "http://localhost:3001"));
        config.addAllowedHeader("*");
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
