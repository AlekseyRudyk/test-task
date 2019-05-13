package com.lobster.testtask;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.lobster.testtask.dao.UserDAO;
import com.lobster.testtask.entities.User;
import com.lobster.testtask.services.impl.UserServiceImpl;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDate;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ComponentScan(basePackages = "com.lobster.testtask")
@DataJpaTest
public class UserServiceTests {

    @Mock
    private UserDAO userDAOMock;

    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenSaveUser_thenSucceed() {
        User user = new User("login",
                    "Aleksey Rudyk",
                            LocalDate.of(1997,9,29),
                            "Male");

        when(userDAOMock.save(any(User.class))).then(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        User savedUser =  userService.saveUser(user);

        verify(userDAOMock).save(user);
        assertNotNull(savedUser.getId());
    }

    @Test(expected = ConstraintViolationException.class)
    public void whenSaveUserWithLoginThatExist_thenThrowException(){
        User user = new User("login",
                "Aleksey Rudyk",
                LocalDate.of(1997,9,29),
                "Male");
        User userWithSameLogin = new User("login",
                "Bogdan Komarov",
                LocalDate.of(1997,3,11),
                "Male");

        when(userDAOMock.save(any(User.class))).then(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });
        when(userDAOMock.save(userWithSameLogin)).thenThrow(ConstraintViolationException.class);

        userService.saveUser(user);
        userService.saveUser(userWithSameLogin);
    }

    @Test
    public void whenUpdateUser_thenSucceed() {
        User user = new User("login",
                "Aleksey Rudyk",
                LocalDate.of(1997,9,29),
                "Male");

        when(userDAOMock.save(any(User.class))).then(invocation -> {
            User savedUser = invocation.getArgument(0);
            if(savedUser.getId()==null)
                savedUser.setId(1L);
            return savedUser;
        });
        when(userDAOMock.findById(anyLong())).then(invocation -> {
            Long id = invocation.getArgument(0);
            user.setId(id);
            return Optional.of(user);
        });

        User savedUser = userService.saveUser(user);

        savedUser.setFullName("Alex Rudyk");
        User updatedUser = userService.updateUser(user, savedUser.getId());

        verify(userDAOMock,times(2)).save(any(User.class));
        verify(userDAOMock).findById(anyLong());

        assertEquals(savedUser.getFullName(),updatedUser.getFullName());
    }

    @Test
    public void whenGetUser_thenSucceed(){
        when(userDAOMock.findById(anyLong())).then(invocation -> {
            Long id = invocation.getArgument(0);
            User user = new User("login",
                    "Aleksey Rudyk",
                    LocalDate.of(1997,9,29),
                    "Male");
            user.setId(id);
            return Optional.of(user);
        });

        User userById = userService.getUser(1L);
        Long returnedId = userById.getId();

        verify(userDAOMock).findById(anyLong());

        assertEquals(1L,returnedId.longValue());
    }
    @Test(expected = UserNotFoundException.class)
    public void whenGetUser_thenThrowException(){
        when(userDAOMock.findById(anyLong())).thenThrow(UserNotFoundException.class);

        userService.getUser(1L);
    }
}
