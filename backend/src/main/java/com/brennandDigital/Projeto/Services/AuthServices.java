package com.brennandDigital.Projeto.Services;

import com.brennandDigital.Projeto.Domain.User;
import com.brennandDigital.Projeto.Repositories.UserRepository;
import com.brennandDigital.Projeto.Services.Exceptions.ResourceNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;

    public AuthServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean checkLogin(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new ResourceNotFoundException("O campo 'nome de usuário' é obrigatório.");
        }

        User user = userRepository
                .findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o nome: " + username));

        if (password == null || password.isBlank()) {
            throw new ResourceNotFoundException("O campo 'senha' é obrigatório.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Senha incorreta");
        }

        return true;
    }
}
