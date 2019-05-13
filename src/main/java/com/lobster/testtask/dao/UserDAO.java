package com.lobster.testtask.dao;

import com.lobster.testtask.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO extends CrudRepository<User,Long> {

    Optional<User> findByLogin(String login);

}
