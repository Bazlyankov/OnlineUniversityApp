package com.studentsystemapp.service.impl;

import com.studentsystemapp.model.binding.UserRegisterBindingModel;
import com.studentsystemapp.repo.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userServiceToTest;


    @Autowired
    private UserRepository userRepository;

    private UserRegisterBindingModel userRegisterBindingModel;

    @BeforeEach
    public void setUp() {

        userRegisterBindingModel = new UserRegisterBindingModel();

        userRegisterBindingModel.setUsername("user");
        userRegisterBindingModel.setPassword("password");
        userRegisterBindingModel.setConfirmPassword("password");
        userRegisterBindingModel.setEmail("user@mail.com");
        userRegisterBindingModel.setFirstName("User");
        userRegisterBindingModel.setLastName("Userov");


        userRepository.deleteAll();
    }

    @Test
    void successfulRegister() {

        assertTrue(userRepository.findAll().isEmpty());

        assertTrue(userServiceToTest.register(userRegisterBindingModel));

        Assertions.assertEquals(1, userRepository.findAll().size());
    }

    @Test
    void unsuccessfulRegisterDuplicate() {

        assertTrue(userRepository.findAll().isEmpty());

        userServiceToTest.register(userRegisterBindingModel);
        assertFalse(userServiceToTest.register(userRegisterBindingModel));

        Assertions.assertEquals(1, userRepository.findAll().size());


        userRegisterBindingModel.setUsername("user2");
        userRegisterBindingModel.setEmail("user2@mail.com");


        assertTrue(userServiceToTest.register(userRegisterBindingModel));
    }


    @Test
    void unsuccessfulRegisterPasswordMismatch() {

        userRegisterBindingModel.setConfirmPassword("passcode");

        userServiceToTest.register(userRegisterBindingModel);

        assertFalse(userServiceToTest.register(userRegisterBindingModel));

    }






    @Test
    void getByUsername() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userServiceToTest.getByUsername("user"));

        userServiceToTest.register(userRegisterBindingModel);

        Assertions.assertNotNull(userServiceToTest.getByUsername("user"));
    }
}