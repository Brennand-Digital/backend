package com.brennandDigital.Projeto.Controllers;

import com.brennandDigital.Projeto.Domain.User;
import com.brennandDigital.Projeto.Services.AuthServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthServices authServices;

    @InjectMocks
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void shouldReturnSuccessWhenLoginIsCorrect() throws Exception {
        User user = new User();
        user.setUserName("usuario");
        user.setPassword("senha123");

        when(authServices.checkLogin("usuario", "senha123")).thenReturn(true);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login bem-sucedido"));
    }

    @Test
    void shouldReturnBadRequestWhenLoginIsInvalid() throws Exception {
        User user = new User();
        user.setUserName("usuario");
        user.setPassword("senhaErrada");

        when(authServices.checkLogin("usuario", "senhaErrada")).thenReturn(false);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Login ou senha inválidos"));
    }

    @Test
    void shouldReturnBadRequestWhenExceptionThrown() throws Exception {
        User user = new User();
        user.setUserName("usuario");
        user.setPassword("senha123");

        when(authServices.checkLogin(anyString(), anyString()))
                .thenThrow(new RuntimeException("Erro inesperado"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erro inesperado"));
    }
}
