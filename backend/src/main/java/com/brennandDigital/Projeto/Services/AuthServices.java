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

    public boolean checkLogin(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new ResourceNotFoundException("O campo 'email' é obrigatório.");
        }

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o email: " + email));

        if (password == null || password.isBlank()) {
            throw new ResourceNotFoundException("O campo 'senha' é obrigatório.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Senha incorreta");
        }

        return true;
    }
}
