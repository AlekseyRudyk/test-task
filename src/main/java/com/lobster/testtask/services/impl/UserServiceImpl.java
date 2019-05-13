package com.lobster.testtask.services.impl;

import com.lobster.testtask.entities.User;
import com.lobster.testtask.dao.UserDAO;
import com.lobster.testtask.UserNotFoundException;
import com.lobster.testtask.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public User saveUser(User newUser) {
        return userDAO.save(newUser);
    }

    @Override
    public User updateUser(User newUser, Long id){
        return userDAO.findById(id)
            .map(user -> {
                user.setFullName(newUser.getFullName());
                user.setDateOfBirth(newUser.getDateOfBirth());
                user.setGender(newUser.getGender());
                return userDAO.save(user);
            })
            .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User getUser(Long id){
        return userDAO.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }





}
