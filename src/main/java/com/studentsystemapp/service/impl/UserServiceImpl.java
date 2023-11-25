package com.studentsystemapp.service.impl;

import com.studentsystemapp.model.entity.BaseUser;
import com.studentsystemapp.model.enums.UserRolesEnum;
import com.studentsystemapp.repo.UserRepository;
import com.studentsystemapp.model.binding.UserRegisterBindingModel;
import com.studentsystemapp.service.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {


    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;


    public UserServiceImpl( PasswordEncoder passwordEncoder, ModelMapper modelMapper, UserRepository userRepository) {

        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }



    @Override
    public boolean register(UserRegisterBindingModel userRegisterBindingModel) {

        String username = userRegisterBindingModel.getUsername();
        String password = userRegisterBindingModel.getPassword();
        String confirmPassword = userRegisterBindingModel.getConfirmPassword();
        Optional<BaseUser> user1 = userRepository.findByUsername(username);
        Optional<BaseUser> user2 = userRepository.findByEmail(userRegisterBindingModel.getEmail());

        if (user1.isPresent() || user2.isPresent() || !password.equals(confirmPassword)) {
            return false;
        }

        BaseUser user = modelMapper.map(userRegisterBindingModel, BaseUser.class);
        user.setPassword(passwordEncoder.encode(userRegisterBindingModel.getPassword()));
        user.setRole(UserRolesEnum.STUDENT);
        userRepository.save(user);

        return true;
    }

    @Override
    public BaseUser getByUsername(String username) {

        return userRepository.getByUsername(username).get();

    }

    @Override
    @Transactional
    public void deleteAll() {
        userRepository.deleteAll();
    }


}
