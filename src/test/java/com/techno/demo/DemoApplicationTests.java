package com.techno.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techno.demo.model.Role;
import com.techno.demo.model.User;
import com.techno.demo.model.request.NamesDTO;
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

import java.util.Arrays;
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
        mockMvc.perform(get("/public"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminEndpointForPublicEndpoint() throws Exception {
        mockMvc.perform(get("/public"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminEndpointForPrivateEndpoint() throws Exception {
        var namesDto = new NamesDTO(Arrays.asList("Test", "Names"));
        var data = objectMapper.writeValueAsString(namesDto);
        mockMvc.perform(post("/private")
                        .content(data).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUserEndpointForPrivateEndpoint() throws Exception {
        var namesDto = new NamesDTO(Arrays.asList("Test", "Names"));
        var data = objectMapper.writeValueAsString(namesDto);
        mockMvc.perform(post("/private")
                        .content(data).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUserDataSave() throws Exception {
        var user = new UserCreateDto(1, "Akash", "1234", "akash",
                java.util.List.of("Admin"));
        var data = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/api/user").content(data).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUserDataFetch() throws Exception {
        var user = new User(1, "Akash", "1234", "akash",
                Set.of(new Role(1L, "Admin")));
        when(userServiceMock.getUserById(anyInt())).thenReturn(user);
        var data = objectMapper.writeValueAsString(user);
        mockMvc.perform(get("/api/user/findById/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void testUserDataFetchByUserName() throws Exception {
        var user = new User(1, "Akash", "1234", "akash",
                Set.of(new Role(1L, "Admin")));
        when(userServiceMock.getUserByUsername(anyString())).thenReturn(Optional.of(user));
        var data = objectMapper.writeValueAsString(user);
        mockMvc.perform(get("/api/user/findByUsername/{username}", user.getUsername()))
                .andExpect(status().isOk()).andExpect(content().json(data));
    }

    @Test
    public void testUserDataFetchByUserNameNotFound() throws Exception {
        var user = new User(1, "Akash", "1234", "akash",
                Set.of(new Role(1L, "Admin")));
        when(userServiceMock.getUserByUsername(user.getUsername())).thenReturn(Optional.of(user));
        var data = objectMapper.writeValueAsString(user);
        mockMvc.perform(get("/api/user/findByUsername/{username}", "someUser"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUserDataModify() throws Exception {
        var user = new User(1, "Akash", "1234", "akash",
                Set.of(new Role(1L, "Admin")));
        var u = userServiceActual.saveUser(user, user.getRoles().stream().map(Role::getName).toList());
        var userDto = new UserCreateDto(1, "Akash1", "1234", "akash",
                List.of("Admin"));
        User updatedUser = userServiceActual.getUserById(1);
        var data = objectMapper.writeValueAsString(userDto);
        var actualData = objectMapper.writeValueAsString(updatedUser);
        mockMvc.perform(put("/api/user/{id}", 1L)
                        .content(data).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testUserDataFetchAndNotFound() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/api/user/findById/{id}", 200L);
        ResultActions perform = mockMvc.perform(mockHttpServletRequestBuilder);
        String contentAsString = perform.andReturn().getResponse().getContentAsString();
        perform.andExpect(status().isNotFound());
    }

    @Test
    public void testUserDelete() throws Exception {
        var user = new User(1, "Akash", "1234", "akash",
                Set.of(new Role(1L, "Admin")));
        userServiceActual.saveUser(user, user.getRoles().stream().map(Role::getName).toList());
        mockMvc.perform(delete("/api/user/{id}", 1L))
                .andExpect(status().isOk()).andExpect(content().string("Deleted"));
    }

}
