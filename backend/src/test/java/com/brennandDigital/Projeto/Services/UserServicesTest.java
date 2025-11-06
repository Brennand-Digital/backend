package com.brennandDigital.Projeto.Services;

import com.brennandDigital.Projeto.Domain.User;
import com.brennandDigital.Projeto.Repositories.UserRepository;
import com.brennandDigital.Projeto.Services.Exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServicesTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServices userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllUsers() {
        User user1 = new User("1", "Gabriel", "senha1");
        User user2 = new User("2", "Maria", "senha2");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnUserById() {
        User user = new User("1", "Gabriel", "senha");
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        User result = userService.findUserId("1");

        assertEquals("Gabriel", result.getUserName());
    }

    @Test
    void shouldThrowWhenUserIdNotFound() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findUserId("1"));
    }

    @Test
    void shouldCreateUser() {
        User user = new User("1", "Gabriel", "senha");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals("Gabriel", result.getUserName());
        assertNotEquals("senha", result.getPassword()); // password deve estar codificado
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldThrowWhenCreateUserWithBlankUsername() {
        User user = new User();
        user.setUserName("  ");
        user.setPassword("senha");

        assertThrows(ResourceNotFoundException.class, () -> userService.createUser(user));
    }

    @Test
    void shouldThrowWhenCreateUserWithBlankPassword() {
        User user = new User();
        user.setUserName("Gabriel");
        user.setPassword("  ");

        assertThrows(ResourceNotFoundException.class, () -> userService.createUser(user));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        User existing = new User("1", "Gabriel", "senhaAntiga");
        User updated = new User("1", "Gabriel Atualizado", "senhaNova");

        when(userRepository.findById("1")).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser("1", updated);

        assertEquals("Gabriel Atualizado", result.getUserName());
        assertNotEquals("senhaNova", result.getPassword()); // password codificado
        verify(userRepository, times(1)).save(existing);
    }

    @Test
    void shouldDeleteUser() {
        doNothing().when(userRepository).deleteById("1");

        assertDoesNotThrow(() -> userService.deleteUser("1"));
        verify(userRepository, times(1)).deleteById("1");
    }

    @Test
    void shouldThrowWhenDeleteUserNotFound() {
        doThrow(new EmptyResultDataAccessException(1)).when(userRepository).deleteById("1");

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser("1"));
    }

    @Test
    void shouldFindByUserName() {
        User user = new User("1", "Gabriel", "senha");
        when(userRepository.findByUserName("Gabriel")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUserName("Gabriel");

        assertTrue(result.isPresent());
        assertEquals("Gabriel", result.get().getUserName());
    }
}
