package com.lobster.testtask.controllers;

import com.lobster.testtask.entities.User;
import com.lobster.testtask.UserNotFoundException;
import com.lobster.testtask.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable(value = "id") Long id) throws UserNotFoundException {
        return userService.getUser(id);
    }

    @PostMapping("/user")
    public User createUser(@Valid @RequestBody User user){
        return userService.saveUser(user);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable(value = "id") Long id,
                           @Valid @RequestBody User user){
        return userService.updateUser(user,id);
    }


}
