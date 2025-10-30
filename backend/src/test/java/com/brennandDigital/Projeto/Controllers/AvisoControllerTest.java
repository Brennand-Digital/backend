package com.brennandDigital.Projeto.Controllers;

import com.brennandDigital.Projeto.Domain.Aviso;
import com.brennandDigital.Projeto.Services.AvisoServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AvisoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AvisoServices avisoServices;

    @InjectMocks
    private AvisoController avisoController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(avisoController).build();
        objectMapper.registerModule(new JavaTimeModule()); // Para serializar LocalDateTime
    }

    @Test
    void shouldReturnListOfAvisos() throws Exception {
        Aviso aviso1 = new Aviso();
        aviso1.setId("1");
        aviso1.setTitulo("Aviso 1");       // define corretamente o título
        aviso1.setDescricao("Descrição 1"); // define corretamente a descrição
        aviso1.setDate(LocalDateTime.now());

        Aviso aviso2 = new Aviso();
        aviso2.setId("2");
        aviso2.setTitulo("Aviso 2");
        aviso2.setDescricao("Descrição 2");
        aviso2.setDate(LocalDateTime.now());

        when(avisoServices.getAllAvisos()).thenReturn(Arrays.asList(aviso1, aviso2));

        mockMvc.perform(get("/avisos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titulo").value("Aviso 1"))
                .andExpect(jsonPath("$[0].descricao").value("Descrição 1"))
                .andExpect(jsonPath("$[1].titulo").value("Aviso 2"))
                .andExpect(jsonPath("$[1].descricao").value("Descrição 2"));
    }


    @Test
    void shouldReturnNoContentWhenListEmpty() throws Exception {
        when(avisoServices.getAllAvisos()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/avisos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnAvisoById() throws Exception {
        Aviso aviso = new Aviso();
        aviso.setId("1");
        aviso.setTitulo("Aviso Teste");      // define corretamente o título
        aviso.setDescricao("Descrição Teste"); // define corretamente a descrição
        aviso.setDate(LocalDateTime.now());

        when(avisoServices.findAvisoById("1")).thenReturn(aviso);

        mockMvc.perform(get("/avisos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Aviso Teste"))
                .andExpect(jsonPath("$.descricao").value("Descrição Teste"));
    }


    @Test
    void shouldCreateAviso() throws Exception {
        Aviso aviso = new Aviso();
        aviso.setTitulo("Novo Aviso");
        aviso.setDescricao("Nova descrição");
        aviso.setDate(LocalDateTime.now());

        Aviso savedAviso = new Aviso();
        savedAviso.setId("1");
        savedAviso.setTitulo("Novo Aviso");
        savedAviso.setDescricao("Nova descrição");
        savedAviso.setDate(LocalDateTime.now());

        when(avisoServices.createAviso(any(Aviso.class))).thenReturn(savedAviso);

        mockMvc.perform(post("/avisos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aviso)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Novo Aviso"))
                .andExpect(jsonPath("$.descricao").value("Nova descrição"));
    }

    @Test
    void shouldUpdateAviso() throws Exception {
        Aviso updated = new Aviso();
        updated.setId("1");
        updated.setTitulo("Atualizado");
        updated.setDescricao("Descrição atualizada");
        updated.setDate(LocalDateTime.now());

        when(avisoServices.updateAviso(eq("1"), any(Aviso.class))).thenReturn(updated);

        mockMvc.perform(put("/avisos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Atualizado"))
                .andExpect(jsonPath("$.descricao").value("Descrição atualizada"));
    }

    @Test
    void shouldDeleteAviso() throws Exception {
        doNothing().when(avisoServices).deleteAviso("1");

        mockMvc.perform(delete("/avisos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(avisoServices, times(1)).deleteAviso("1");
    }
}
