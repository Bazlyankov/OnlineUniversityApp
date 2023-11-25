package com.studentsystemapp.service.impl;

import com.studentsystemapp.model.binding.UserRegisterBindingModel;
import com.studentsystemapp.model.entity.BaseUser;
import com.studentsystemapp.model.enums.UserRolesEnum;
import com.studentsystemapp.repo.UserRepository;
import com.studentsystemapp.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userServiceToTest;


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


        userServiceToTest.deleteAll();
    }

    @Test
    void successfulRegister() {

        assertThrows(NoSuchElementException.class, () -> userServiceToTest.getByUsername("user"));

        assertTrue(userServiceToTest.register(userRegisterBindingModel));

        Assertions.assertNotNull( userServiceToTest.getByUsername("user"));
    }

    @Test
    void unsuccessfulRegisterDuplicate() {

        assertThrows(NoSuchElementException.class, () -> userServiceToTest.getByUsername("user"));


        userServiceToTest.register(userRegisterBindingModel);
        assertFalse(userServiceToTest.register(userRegisterBindingModel));

        assertThrows(NoSuchElementException.class, () -> userServiceToTest.getByUsername("user2"));

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