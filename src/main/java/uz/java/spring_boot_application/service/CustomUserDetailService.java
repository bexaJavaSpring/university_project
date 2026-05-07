package uz.java.spring_boot_application.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.java.spring_boot_application.entities.User;
import uz.java.spring_boot_application.exception.GenericNotFoundException;
import uz.java.spring_boot_application.repository.UserRepository;
import uz.java.spring_boot_application.security.CustomUserDetails;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username); // shu yerda har safar username orqali user qidirilyapti shu uchun bizlar username field ga index qoshdik
        if (user == null)
            throw new GenericNotFoundException("user.not.found");

        Set<GrantedAuthority> authorities = new HashSet<>();
        if (Objects.nonNull(user.getRoles()))
            user.getRoles().forEach(role -> {
                authorities.add(authority.apply(role.getAuthority()));
            });
        return new CustomUserDetails(user, authorities);
    }

    private final static Function<String, SimpleGrantedAuthority> authority = SimpleGrantedAuthority::new;

}
