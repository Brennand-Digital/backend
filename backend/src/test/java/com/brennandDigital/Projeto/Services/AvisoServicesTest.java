package com.brennandDigital.Projeto.Services;

import com.brennandDigital.Projeto.Domain.Aviso;
import com.brennandDigital.Projeto.Repositories.AvisoRepository;
import com.brennandDigital.Projeto.Services.Exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvisoServicesTest {

    @Mock
    private AvisoRepository avisoRepository;

    @InjectMocks
    private AvisoServices avisoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllAvisos() {
        Aviso aviso1 = new Aviso("1", "Aviso A", "Descricao A", LocalDateTime.now());
        Aviso aviso2 = new Aviso("2", "Aviso B", "Descricao B", LocalDateTime.now());

        when(avisoRepository.findAll()).thenReturn(List.of(aviso1, aviso2));

        List<Aviso> result = avisoService.getAllAvisos();

        assertEquals(2, result.size());
        verify(avisoRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnAvisoById() {
        Aviso mockAviso = new Aviso();
        mockAviso.setId("1");
        mockAviso.setTitulo("Aviso Teste");
        mockAviso.setDescricao("Descrição Teste");

        when(avisoRepository.findById("1")).thenReturn(Optional.of(mockAviso));

        Aviso result = avisoService.findAvisoById("1");

        assertEquals("Aviso Teste", result.getTitulo());
        assertEquals("Descrição Teste", result.getDescricao());
    }

    @Test
    void shouldThrowExceptionWhenAvisoNotFound() {
        when(avisoRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> avisoService.findAvisoById("99"));
    }

    @Test
    void shouldCreateAviso() {
        Aviso aviso = new Aviso("1", "Descricao", "Novo Aviso", LocalDateTime.now());
        when(avisoRepository.save(aviso)).thenReturn(aviso);

        Aviso saved = avisoService.createAviso(aviso);

        assertNotNull(saved);
        assertEquals("Novo Aviso", saved.getTitulo());
        assertEquals("Descricao", saved.getDescricao());
        verify(avisoRepository, times(1)).save(aviso);
    }

    @Test
    void shouldUpdateAvisoSuccessfully() throws Exception {
        Aviso existing = new Aviso("1", "Antiga descrição", "Antigo título", LocalDateTime.now());
        Aviso updated = new Aviso("1", "Nova descrição", "Novo Título", LocalDateTime.now());

        when(avisoRepository.findById("1")).thenReturn(Optional.of(existing));
        when(avisoRepository.save(any(Aviso.class))).thenReturn(updated);

        Aviso result = avisoService.updateAviso("1", updated);

        assertEquals("Novo Título", result.getTitulo());
        assertEquals("Nova descrição", result.getDescricao());
        verify(avisoRepository, times(1)).save(existing);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentAviso() {
        when(avisoRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> avisoService.updateAviso("99", new Aviso()));
    }

    @Test
    void shouldDeleteAvisoSuccessfully() {
        doNothing().when(avisoRepository).deleteById("1");

        assertDoesNotThrow(() -> avisoService.deleteAviso("1"));
        verify(avisoRepository, times(1)).deleteById("1");
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonexistentAviso() {
        doThrow(new EmptyResultDataAccessException(1))
                .when(avisoRepository).deleteById("99");

        assertThrows(ResourceNotFoundException.class, () -> avisoService.deleteAviso("99"));
    }
}
