package com.techno.demo.util;

import com.techno.demo.model.entity.Role;
import com.techno.demo.model.entity.User;

import java.util.Set;

public class Constants {
    public static final User ADMIN_USER =  new User(1, "Akash", "1234", "akash",
            Set.of(new Role(1L, "ADMIN")));
    public static final User USER_USER =  new User(2, "Bhuvaneshvar", "1234", "bhuvancom",
            Set.of(new Role(1L, "USER")));
}
