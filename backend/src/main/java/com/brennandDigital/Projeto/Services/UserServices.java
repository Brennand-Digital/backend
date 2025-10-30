package com.brennandDigital.Projeto.Services;

import com.brennandDigital.Projeto.Domain.User;
import com.brennandDigital.Projeto.Repositories.UserRepository;
import com.brennandDigital.Projeto.Services.Exceptions.ResourceNotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UserServices {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;

    public UserServices(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User findUserId(String userId){
        Optional<User> user = userRepository.findById(userId);
        return user.orElseThrow(() -> new ResourceNotFoundException(userId));
    }

    public User createUser(User userDetails){
        if (userDetails.getUserName() == null || userDetails.getUserName().isBlank()) {
            throw new ResourceNotFoundException("O campo 'nome de usuário' é obrigatório.");
        }
        if (userDetails.getPassword() == null || userDetails.getPassword().isBlank()) {
            throw new ResourceNotFoundException("O campo 'nome de usuário' é obrigatório.");
        }
        userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));

        return userRepository.save(userDetails);
    }

    public User updateUser(String userId, User userDetails) throws Exception {
        User user = userRepository
                .findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + userId));

            user.setUserName(userDetails.getUserName());
        if (userDetails.getPassword() != null && !userDetails.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
            return userRepository.save(user);
    }

    public void deleteUser(String userId){
        try {
            userRepository.deleteById(userId);
        }
        catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    public Optional<User> findByUserName(String gabriel) {
        return userRepository.findByUserName(gabriel);
    }
}
