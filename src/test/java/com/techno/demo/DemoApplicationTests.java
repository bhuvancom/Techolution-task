package com.techno.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techno.demo.model.entity.Role;
import com.techno.demo.model.entity.User;
import com.techno.demo.model.request.UserCreateDto;
import com.techno.demo.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userServiceMock;

    @Autowired
    private UserService userServiceActual;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "USER")
    public void testUserEndpointForPublicEndpoint() throws Exception {
        var user = new User(1, "Akash", "1234", "akash",
                Set.of(new Role(1L, "USER")));
        when(userServiceMock.getUserById(anyInt())).thenReturn(user);
        mockMvc.perform(get("/api/user/findById/{id}",1))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(roles = "USER")
    public void testUserEndpointForPrivateEndpoint() throws Exception {
        var user = new UserCreateDto(1, "Akash", "1234", "akash",
                java.util.List.of("Admin"));
        var data = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/api/user/create").content(data).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUserDataSave() throws Exception {
        var user = new UserCreateDto(1, "Akash", "1234", "akash",
                java.util.List.of("Admin"));
        var data = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/api/user/create").content(data).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUserDataFetch() throws Exception {
        var user = new User(1, "Akash", "1234", "akash",
                Set.of(new Role(1L, "Admin")));
        when(userServiceMock.getUserById(anyInt())).thenReturn(user);
        var data = objectMapper.writeValueAsString(user);
        mockMvc.perform(get("/api/user/findById/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUserDataFetchForAdmin() throws Exception {
        var user = new User(1, "Akash", "1234", "akash",
                Set.of(new Role(1L, "Admin")));
        when(userServiceMock.getUserById(anyInt())).thenReturn(user);
        var data = objectMapper.writeValueAsString(user);
        mockMvc.perform(get("/api/user/findById/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUserDataFetchByUserNameForAdmin() throws Exception {
        var user = new User(1, "Akash", "1234", "akash",
                Set.of(new Role(1L, "Admin")));
        when(userServiceMock.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        var data = objectMapper.writeValueAsString(user);
        mockMvc.perform(get("/api/user/findByUsername/{username}", user.getUsername()))
                .andExpect(status().isOk()).andExpect(content().json(data));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUserDataFetchByUserNameForUser() throws Exception {
        var user = new User(1, "Akash", "1234", "akash",
                Set.of(new Role(1L, "Admin")));
        when(userServiceMock.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        var data = objectMapper.writeValueAsString(user);
        mockMvc.perform(get("/api/user/findByUsername/{username}", user.getUsername()))
                .andExpect(status().isOk()).andExpect(content().json(data));
    }

    @Test
    public void testUserDataFetchByUserNameForNoRoleUnAuthorized() throws Exception {
        var user = new User(1, "Akash", "1234", "akash",
                Set.of(new Role(1L, "Admin")));
        when(userServiceMock.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        var data = objectMapper.writeValueAsString(user);
        mockMvc.perform(get("/api/user/findByUsername/{username}", user.getUsername()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUserDataFetchByUserNameNotFound() throws Exception {
        var user = new User(1, "Akash", "1234", "akash",
                Set.of(new Role(1L, "Admin")));
        when(userServiceMock.getUserByUsername(user.getUsername())).thenReturn(Optional.of(user));
        var data = objectMapper.writeValueAsString(user);
        mockMvc.perform(get("/api/user/findByUsername/{username}", "someUser"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUserDataFetchByUserNameNotFoundForAdmin() throws Exception {
        var user = new User(1, "Akash", "1234", "akash",
                Set.of(new Role(1L, "Admin")));
        when(userServiceMock.getUserByUsername(user.getUsername())).thenReturn(Optional.of(user));
        var data = objectMapper.writeValueAsString(user);
        mockMvc.perform(get("/api/user/findByUsername/{username}", "someUser"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUserDataModify() throws Exception {
        var user = new User(1, "Akash", "1234", "akash",
                Set.of(new Role(1L, "Admin")));
        var u = userServiceActual.saveUser(user, user.getRoles().stream().map(Role::getName).toList());
        var userDto = new UserCreateDto(1, "Akash1", "1234", "akash",
                List.of("Admin"));
        User updatedUser = userServiceActual.getUserById(1);
        var data = objectMapper.writeValueAsString(userDto);
        var actualData = objectMapper.writeValueAsString(updatedUser);
        mockMvc.perform(put("/api/user/update/{id}", 1L)
                        .content(data).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUserDataModifyForUser() throws Exception {
        var user = new User(1, "Akash", "1234", "akash",
                Set.of(new Role(1L, "Admin")));
        var u = userServiceActual.saveUser(user, user.getRoles().stream().map(Role::getName).toList());
        var userDto = new UserCreateDto(1, "Akash1", "1234", "akash",
                List.of("Admin"));
        User updatedUser = userServiceActual.getUserById(1);
        var data = objectMapper.writeValueAsString(userDto);
        var actualData = objectMapper.writeValueAsString(updatedUser);
        mockMvc.perform(put("/api/user/update/{id}", 1L)
                        .content(data).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    public void testUserDataFetchAndNotFound() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/api/user/findById/{id}", 200L);
        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        String contentAsString = perform.andReturn().getResponse().getContentAsString();
        perform.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUserDelete() throws Exception {
        var user = new User(1, "Akash", "1234", "akash",
                Set.of(new Role(1L, "Admin")));
        userServiceActual.saveUser(user, user.getRoles().stream().map(Role::getName).toList());
        mockMvc.perform(delete("/api/user/delete/{id}", 1L))
                .andExpect(status().isOk()).andExpect(content().string("Deleted"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUserDeleteForUser() throws Exception {
        var user = new User(1, "Akash", "1234", "akash",
                Set.of(new Role(1L, "Admin")));
        userServiceActual.saveUser(user, user.getRoles().stream().map(Role::getName).toList());
        mockMvc.perform(delete("/api/user/delete/{id}", 1L))
                .andExpect(status().isForbidden());
    }

}
