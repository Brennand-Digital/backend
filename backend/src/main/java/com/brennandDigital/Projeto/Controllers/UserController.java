package com.brennandDigital.Projeto.Controllers;

import com.brennandDigital.Projeto.Domain.User;
import com.brennandDigital.Projeto.Services.UserServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@Slf4j
public class UserController {

    private final UserServices userServices;

    public UserController(UserServices userServices){
        this.userServices = userServices;
    }

    @GetMapping
    public ResponseEntity<List<User>> userAvisos(){
        List<User> users = userServices.getAllUsers();
        if(users.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<User> findUserById(@PathVariable String userId){
        User user = userServices.findUserId(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<User> deleteUserById(@PathVariable String userId){
        userServices.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody User userDetails){
        try{
            userDetails = userServices.updateUser(userId, userDetails);
            return new ResponseEntity<>(userDetails, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user){
        user = userServices.createUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
