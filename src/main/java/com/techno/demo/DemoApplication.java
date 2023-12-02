package com.techno.demo;

import com.techno.demo.model.Role;
import com.techno.demo.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        var userRole = new Role();
        userRole.setName("USER");
        roleRepository.save(userRole);
        var adminRole = new Role();
        adminRole.setName("ADMIN");
        roleRepository.save(adminRole);
    }
}
