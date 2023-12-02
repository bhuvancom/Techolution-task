package com.techno.demo.model.request;

import com.techno.demo.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {
    private Integer id;
    private String name;
    private String username;
    private String password;
    private List<String> roles;

    public User toUser() {
        User u = new User();
        u.setId(id);
        u.setName(name);
        u.setPassword(password);
        u.setUsername(username);
        return u;
    }
}
