package com.group7.cafemanagementsystem.Security;

import com.group7.cafemanagementsystem.Repository.UserRepository;
import com.group7.cafemanagementsystem.model.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not exist"));

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
        return new User(username, user.getPassword(), Collections.singleton(authority));
    }
}
