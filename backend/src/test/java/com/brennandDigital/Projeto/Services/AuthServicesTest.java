package com.brennandDigital.Projeto.Services;

import com.brennandDigital.Projeto.Domain.User;
import com.brennandDigital.Projeto.Repositories.UserRepository;
import com.brennandDigital.Projeto.Services.Exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServicesTest {

    private UserRepository userRepository;
    private AuthServices authServices;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        authServices = new AuthServices(userRepository);
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void shouldReturnTrueWhenLoginIsValid() {
        User user = new User();
        user.setEmail("gabriel@email.com");
        user.setPassword(passwordEncoder.encode("1234"));

        when(userRepository.findByEmail("gabriel@email.com")).thenReturn(Optional.of(user));

        boolean result = authServices.checkLogin("gabriel@email.com", "1234");

        assertTrue(result);
        verify(userRepository, times(1)).findByEmail("gabriel@email.com");
    }

    @Test
    void shouldThrowWhenEmailIsNull() {
        assertThrows(ResourceNotFoundException.class, () -> authServices.checkLogin(null, "1234"));
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void shouldThrowWhenEmailIsBlank() {
        assertThrows(ResourceNotFoundException.class, () -> authServices.checkLogin("   ", "1234"));
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(userRepository.findByEmail("gabriel@email.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authServices.checkLogin("gabriel@email.com", "1234"));
        verify(userRepository, times(1)).findByEmail("gabriel@email.com");
    }

    @Test
    void shouldThrowWhenPasswordIsNull() {
        User user = new User();
        user.setEmail("gabriel@email.com");
        user.setPassword(passwordEncoder.encode("1234"));

        when(userRepository.findByEmail("gabriel@email.com")).thenReturn(Optional.of(user));

        assertThrows(ResourceNotFoundException.class, () -> authServices.checkLogin("gabriel@email.com", null));
    }

    @Test
    void shouldThrowWhenPasswordIsBlank() {
        User user = new User();
        user.setEmail("gabriel@email.com");
        user.setPassword(passwordEncoder.encode("1234"));

        when(userRepository.findByEmail("gabriel@email.com")).thenReturn(Optional.of(user));

        assertThrows(ResourceNotFoundException.class, () -> authServices.checkLogin("gabriel@email.com", "   "));
    }

    @Test
    void shouldThrowWhenPasswordIsIncorrect() {
        User user = new User();
        user.setEmail("gabriel@email.com");
        user.setPassword(passwordEncoder.encode("1234"));

        when(userRepository.findByEmail("gabriel@email.com")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> authServices.checkLogin("gabriel@email.com", "senhaErrada"));
    }
}
