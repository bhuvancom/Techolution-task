package com.techno.demo.services;

import com.techno.demo.model.entity.User;
import com.techno.demo.repositories.RoleRepository;
import com.techno.demo.repositories.UserRepository;
import com.techno.demo.util.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class UserServiceTest {
    @InjectMocks
    private UserService userServiceMock;
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;

    @Test
    void saveUser() {
        when(userRepository.save(Constants.USER_USER)).thenReturn(Constants.USER_USER);
        User result = userServiceMock.saveUser(Constants.USER_USER,List.of("ADMIN"));
        assertNotNull(result);
    }


    @Test
    void getUserById() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(Constants.USER_USER));
        User result = userServiceMock.getUserById(1);
        assertNotNull(result);
    }

    @Test
    void getUserByUsername() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(Constants.USER_USER));
        Optional<User> result = userServiceMock.getUserByUsername("akash");
        assertNotNull(result.get());
    }

    @Test
    void getUsers() {
        when(userRepository.findAll()).thenReturn(List.of(Constants.USER_USER, Constants.ADMIN_USER));
        List<User> users = userServiceMock.getUsers();
        Assertions.assertEquals(2, users.size());
    }
}