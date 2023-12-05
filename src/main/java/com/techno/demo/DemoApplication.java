package com.techno.demo;

import com.techno.demo.model.entity.Role;
import com.techno.demo.model.entity.User;
import com.techno.demo.repositories.RoleRepository;
import com.techno.demo.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

@SpringBootApplication
@Slf4j
public class DemoApplication implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;


    public static void main(String[] args) {
        log.info("Application is starting....");
        SpringApplication.run(DemoApplication.class, args);
        log.info("Application started..");
    }

    @Override
    public void run(String... args) {
        log.debug("Adding few data in db for initial use");
        var userRole = new Role(1L, "USER");
        roleRepository.save(userRole);
        var adminRole = new Role(2L, "ADMIN");
        roleRepository.save(adminRole);

        var user = new User(1, "Bhuvaneshvar", "12345", "bhuvancom", Set.of(adminRole, userRole));
        userService.saveUser(user, user.getRoles().stream().map(Role::getName).toList());
        log.debug("Data saved in database");
    }
}
