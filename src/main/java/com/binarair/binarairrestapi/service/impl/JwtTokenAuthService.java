package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.model.entity.User;
import com.binarair.binarairrestapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtTokenAuthService implements UserDetailsService {

    private final static Logger log = LoggerFactory.getLogger(JwtTokenAuthService.class);

    private final UserRepository userRepository;

    @Autowired
    public JwtTokenAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Set<SimpleGrantedAuthority> getAuthorities(User user) {
        log.info("Generate roles by becoming set of simple granted authorities");
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = new HashSet<>();
        simpleGrantedAuthorities.add(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().getRole().name())
        );

        return simpleGrantedAuthorities;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("find user by email {} ", username);
        User user =  userRepository.findByEmail(username).orElseThrow(() -> {
            log.warn("User account is not found");
            throw new DataNotFoundException(String.format("User with email %s not found", username));
        });
        log.info("successful getting users with email {} ", username);
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                getAuthorities(user)
        );

    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            log.warn("User account is not found");
            throw new DataNotFoundException(String.format("User with email %s not found", email));
        });
    }
}
