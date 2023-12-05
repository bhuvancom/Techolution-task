package com.techno.demo.services;

import com.techno.demo.exception.ResourceNotFoundException;
import com.techno.demo.model.entity.Role;
import com.techno.demo.model.entity.User;
import com.techno.demo.model.request.UserCreateDto;
import com.techno.demo.repositories.RoleRepository;
import com.techno.demo.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    final private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User updateUser(Integer id, UserCreateDto dto) {
        if (id == null || dto.getId() == null || id.intValue() != dto.getId().intValue()) {
            log.error("incoming user id was having issue {}", dto);
            throw new IllegalArgumentException("Missing or invalid userId");
        }

        return userRepository.findById(dto.getId()).map(u -> saveUser(dto.toUser(), dto.getRoles())).orElseThrow(() ->
                new ResourceNotFoundException("Username -> " + dto.getUsername() + " was not found")
        );
    }

    public User saveUser(User user, List<String> roleNames) {
        log.debug("inside save user");
        Set<Role> roles = new HashSet<>();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        roleNames.forEach(roleName -> {
            roleRepository.findByName(roleName).ifPresent(roles::add);
        });
        if (roles.isEmpty()) {
            log.debug("Roles are sent in request adding default one to user {}", user.getUsername());
            roleRepository.findByName("USER").ifPresent(roles::add);
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        log.debug("Going to delete user by id {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id -> " + id + " was not found"));
        userRepository.delete(user);
    }

    public User getUserById(Integer id) {
        log.debug("Going to fetch user by id {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id -> " + id + " was not found"));
        user.getRoles();
        return user;
    }

    public Optional<User> getUserByUsername(String username) {
        log.debug("Going to get user by user name {}", username);
        return userRepository.findByUsername(username);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
