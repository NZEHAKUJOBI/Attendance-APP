package com.library.fullstackbackend.controller;

import com.library.fullstackbackend.exception.UserNotFoundException;
import com.library.fullstackbackend.model.User;
import com.library.fullstackbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.events.Event;

import java.util.List;
import java.util.UUID;


@RestController
@CrossOrigin("http://localhost:3000")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/user")
    User newUser(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }

    @GetMapping("/users")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    User getUserById(@PathVariable UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @PutMapping("/user/{id}")
    User updateUser(@RequestBody User newUser, @PathVariable UUID id) {
        return userRepository.findById(id)
                .map(user -> {

                    user.setFullName(newUser.getFullName());
                    user.setDesignation(newUser.getDesignation());
                    user.setFacility(newUser.getFacility());
                    user.setPhone_number(newUser.getPhone_number());
                    return userRepository.save(user);
                }).orElseThrow(() -> new UserNotFoundException(id));
    }

    @DeleteMapping("/user/{id}")
    String deleteUser(@PathVariable UUID id){
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        return  "User with id "+id+" has been deleted success.";
    }



}
