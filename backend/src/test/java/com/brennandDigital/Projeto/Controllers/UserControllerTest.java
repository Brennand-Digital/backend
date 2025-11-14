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

class UserControllerTest {

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
        User user1 = new User("1", "Gabriel", "gabriel@email.com", "senha1");
        User user2 = new User("2", "Maria", "maria@email.com", "senha2");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnUserById() {
        User user = new User("1", "Gabriel", "gabriel@email.com", "senha");
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
        User user = new User("1", "Gabriel", "gabriel@email.com", "senha");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals("Gabriel", result.getUserName());
        assertEquals("gabriel@email.com", result.getEmail());
        assertNotEquals("senha", result.getPassword()); // deve estar criptografada
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldThrowWhenCreateUserWithBlankUsername() {
        User user = new User(null, "  ", "teste@email.com", "senha");

        assertThrows(ResourceNotFoundException.class, () -> userService.createUser(user));
    }

    @Test
    void shouldThrowWhenCreateUserWithBlankPassword() {
        User user = new User(null, "Gabriel", "teste@email.com", "  ");

        assertThrows(ResourceNotFoundException.class, () -> userService.createUser(user));
    }

    @Test
    void shouldThrowWhenCreateUserWithInvalidEmail() {
        User user = new User(null, "Gabriel", "email-invalido", "senha");

        assertThrows(ResourceNotFoundException.class, () -> userService.createUser(user));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        User existing = new User("1", "Gabriel", "gabriel@email.com", "senhaAntiga");
        User updated = new User("1", "Gabriel Atualizado", "gabriel.atualizado@email.com", "senhaNova");

        when(userRepository.findById("1")).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser("1", updated);

        assertEquals("Gabriel Atualizado", result.getUserName());
        assertEquals("gabriel.atualizado@email.com", result.getEmail());
        assertNotEquals("senhaNova", result.getPassword()); // deve estar criptografada
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
        User user = new User("1", "Gabriel", "gabriel@email.com", "senha");
        when(userRepository.findByUserName("Gabriel")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUserName("Gabriel");

        assertTrue(result.isPresent());
        assertEquals("Gabriel", result.get().getUserName());
    }
}
