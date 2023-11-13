package com.studentsystemapp.service.impl;

import com.studentsystemapp.model.binding.StudentRegisterBindingModel;
import com.studentsystemapp.model.entity.*;
import com.studentsystemapp.model.enums.UserRolesEnum;
import com.studentsystemapp.model.view.StudentViewModel;
import com.studentsystemapp.repo.UserRepository;
import com.studentsystemapp.service.StudentService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {


    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public StudentServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean remove(String username) {
        return userRepository.deleteByUsername(username);
    }



    @Override
    public boolean add(StudentRegisterBindingModel studentRegisterBindingModel) {
        Optional<BaseUser> optionalStudent = userRepository.getByUsername(
                studentRegisterBindingModel.getUsername());

        if (optionalStudent.isPresent() ||
        !studentRegisterBindingModel.getPassword().equals(studentRegisterBindingModel.getConfirmPassword())) {
            return false;
        }


        userRepository.saveAndFlush(optionalStudent.get());

        return true;
    }

    @Override
    public List<BaseUser> getAll() {
        return userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() == UserRolesEnum.STUDENT)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaseUser> getAllByCourse(Course course) {
        return userRepository.getAllByCoursesContaining(course);
    }

    @Override
    public void addGrade(Long studentId, Grade grade) {

    }

    @Override
    public StudentViewModel getById(Long id) {
        Optional<BaseUser> optionalStudent = userRepository.findById(id);

        if (optionalStudent.isEmpty()) {
            return null;
        }

        StudentViewModel studentViewModel = modelMapper.map(optionalStudent.get(), StudentViewModel.class);
        return studentViewModel;
    }

    @Override
    @Transactional
    public void makeTeacher(Long id) {

        Optional<BaseUser> optionalStudent = userRepository.findById(id);

        if (optionalStudent.isEmpty()) return;

        BaseUser student = optionalStudent.get();
        student.setRole(UserRolesEnum.TEACHER);
        userRepository.saveAndFlush(student);

    }


}
