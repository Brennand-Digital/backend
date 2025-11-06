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
        user.setUserName("gabriel");
        user.setPassword(passwordEncoder.encode("1234"));

        when(userRepository.findByUserName("gabriel")).thenReturn(Optional.of(user));

        boolean result = authServices.checkLogin("gabriel", "1234");

        assertTrue(result);
        verify(userRepository, times(1)).findByUserName("gabriel");
    }

    @Test
    void shouldThrowWhenUsernameIsNull() {
        assertThrows(ResourceNotFoundException.class, () -> authServices.checkLogin(null, "1234"));
        verify(userRepository, never()).findByUserName(anyString());
    }

    @Test
    void shouldThrowWhenUsernameIsBlank() {
        assertThrows(ResourceNotFoundException.class, () -> authServices.checkLogin("   ", "1234"));
        verify(userRepository, never()).findByUserName(anyString());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(userRepository.findByUserName("gabriel")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authServices.checkLogin("gabriel", "1234"));
        verify(userRepository, times(1)).findByUserName("gabriel");
    }

    @Test
    void shouldThrowWhenPasswordIsNull() {
        User user = new User();
        user.setUserName("gabriel");
        user.setPassword(passwordEncoder.encode("1234"));

        when(userRepository.findByUserName("gabriel")).thenReturn(Optional.of(user));

        assertThrows(ResourceNotFoundException.class, () -> authServices.checkLogin("gabriel", null));
    }

    @Test
    void shouldThrowWhenPasswordIsBlank() {
        User user = new User();
        user.setUserName("gabriel");
        user.setPassword(passwordEncoder.encode("1234"));

        when(userRepository.findByUserName("gabriel")).thenReturn(Optional.of(user));

        assertThrows(ResourceNotFoundException.class, () -> authServices.checkLogin("gabriel", "   "));
    }

    @Test
    void shouldThrowWhenPasswordIsIncorrect() {
        User user = new User();
        user.setUserName("gabriel");
        user.setPassword(passwordEncoder.encode("1234"));

        when(userRepository.findByUserName("gabriel")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> authServices.checkLogin("gabriel", "senhaErrada"));
    }
}
