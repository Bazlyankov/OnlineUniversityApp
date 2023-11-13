package com.studentsystemapp.service.impl;

import com.studentsystemapp.model.entity.BaseUser;
import com.studentsystemapp.model.enums.UserRolesEnum;
import com.studentsystemapp.repo.UserRepository;
import com.studentsystemapp.service.TeacherService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final UserRepository userRepository;

    public TeacherServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<BaseUser> getAll() {
        return userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() == UserRolesEnum.TEACHER)
                .collect(Collectors.toList());
    }

}
