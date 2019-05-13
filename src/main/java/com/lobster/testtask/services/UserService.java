package com.lobster.testtask.services;

import com.lobster.testtask.entities.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User saveUser(User newUser);

    User updateUser(User newUser, Long id);

    User getUser(Long id);

}
