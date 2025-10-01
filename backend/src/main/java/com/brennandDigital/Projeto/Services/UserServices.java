package com.brennandDigital.Projeto.Services;

import com.brennandDigital.Projeto.Domain.User;
import com.brennandDigital.Projeto.Repositories.UserRepository;
import com.brennandDigital.Projeto.Services.Exceptions.ResourceNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServices {

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

    public User createUser(User user){
        return userRepository.save(user);
    }

    public User updateUser(String userId, User userDetails) throws Exception {
        Optional<User> userOpt = userRepository.findById(userId);

        if(userOpt.isPresent()){
            User user = findUserId(userId);
            user.setUserName(userDetails.getUserName());
            user.setPassword(userDetails.getPassword());
            return userRepository.save(user);
        }
        throw new ResourceNotFoundException("Usuário não encontrado com o ID: " + userId);
    }

    public void deleteUser(String userId){
        try {
            userRepository.deleteById(userId);
        }
        catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

}
