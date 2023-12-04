package com.techno.demo.services;

import com.techno.demo.exception.ResourceNotFoundException;
import com.techno.demo.model.Role;
import com.techno.demo.model.User;
import com.techno.demo.model.request.UserCreateDto;
import com.techno.demo.repositories.RoleRepository;
import com.techno.demo.repositories.UserRepository;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Log
public class UserService {
    final private UserRepository userRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    private final RoleRepository roleRepository;

    public User updateUser(Integer id, UserCreateDto dto) {
        if (id == null || dto.getId() == null || id.intValue() != dto.getId().intValue()) {
            throw new IllegalArgumentException("Missing or invalid userId");
        }

        return userRepository.findById(dto.getId()).map(u -> saveUser(dto.toUser(), dto.getRoles())).orElseThrow(() ->
                new ResourceNotFoundException("Username -> " + dto.getUsername() + " was not found")
        );
    }

    public User saveUser(User user, List<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        roleNames.forEach(roleName -> {
            roleRepository.findByName(roleName).ifPresent(roles::add);
        });
        if (roles.isEmpty()) {
            roleRepository.findByName("USER").ifPresent(roles::add);
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id -> " + id + " was not found"));
        userRepository.delete(user);
    }

    public User getUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id -> " + id + " was not found"));
        user.getRoles();
        return user;
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
