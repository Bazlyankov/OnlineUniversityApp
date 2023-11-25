package com.studentsystemapp.service;

import com.studentsystemapp.model.binding.UserLoginBindingModel;
import com.studentsystemapp.model.binding.UserRegisterBindingModel;
import com.studentsystemapp.model.entity.BaseUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {


    boolean register(UserRegisterBindingModel userRegisterBindingModel);

    BaseUser getByUsername(String username);

    void deleteAll();
}
