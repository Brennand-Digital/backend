package com.brennandDigital.Projeto.Controllers;

import com.brennandDigital.Projeto.Domain.User;
import com.brennandDigital.Projeto.Services.UserServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServices userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllUsers() throws Exception {
        List<User> mockUsers = List.of(
                new User("1", "Gabriel", "123"),
                new User("2", "Maria", "abc")
        );
        when(userService.getAllUsers()).thenReturn(mockUsers);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userName").value("Gabriel"))
                .andExpect(jsonPath("$[1].userName").value("Maria"));
    }

    @Test
    void shouldReturnUserById() throws Exception {
        User mockUser = new User("1", "Gabriel", "123");
        when(userService.findUserId("1")).thenReturn(mockUser);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("Gabriel"));
    }

    @Test
    void shouldCreateNewUser() throws Exception {
        User newUser = new User(null, "João", "senha123");
        User savedUser = new User("99", "João", "senha123");
        when(userService.createUser(newUser)).thenReturn(savedUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated()) // ⬅ status corrigido
                .andExpect(jsonPath("$.userName").value("João"));
    }
}
