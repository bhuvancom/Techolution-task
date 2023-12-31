package com.techno.demo.services;

import com.techno.demo.model.entity.User;
import com.techno.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("User to find by name is {}", username);
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username " + username));

        Set<GrantedAuthority> authorities = user.getRoles()
                .stream().map((role) -> new SimpleGrantedAuthority("ROLE_" + role.getName())).collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
