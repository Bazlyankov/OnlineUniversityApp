package com.studentsystemapp.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.studentsystemapp.model.binding.StudentRegisterBindingModel;
import com.studentsystemapp.model.entity.*;
import com.studentsystemapp.model.enums.UserRolesEnum;
import com.studentsystemapp.model.view.StudentViewModel;
import com.studentsystemapp.repo.UserRepository;
import com.studentsystemapp.service.StudentService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.NotActiveException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {


    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final Cloudinary cloudinary;

    public StudentServiceImpl(UserRepository userRepository, ModelMapper modelMapper, Cloudinary cloudinary) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.cloudinary = cloudinary;
    }

    @Override
    public int remove(String username) {
        return userRepository.deleteByUsername(username);
    }



    @Override
    @Transactional
    public boolean add(StudentRegisterBindingModel studentRegisterBindingModel) {
        Optional<BaseUser> optionalStudent = userRepository.getByUsername(
                studentRegisterBindingModel.getUsername());

        if (optionalStudent.isPresent() ||
        !studentRegisterBindingModel.getPassword().equals(studentRegisterBindingModel.getConfirmPassword())) {
            return false;
        }

        BaseUser baseUser = modelMapper.map(studentRegisterBindingModel, BaseUser.class);
        baseUser.setRole(UserRolesEnum.STUDENT);

        userRepository.save(baseUser);

        return true;
    }

    @Override
    public List<BaseUser> getAllStudents() {
        return userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() == UserRolesEnum.STUDENT)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaseUser> getAllByCourse(Course course) {
        return userRepository.findAll().stream()
                .filter(u ->  u.getEnrollments().stream()
                        .filter(e -> e.getCourse().equals(course)).toList()
                        .size() > 0).collect(Collectors.toList());
    }



    @Override
    public StudentViewModel getById(Long id) {
        Optional<BaseUser> optionalStudent = userRepository.findById(id);

        if (optionalStudent.isEmpty()) {
            throw new NoSuchElementException();
        }

        return modelMapper.map(optionalStudent.get(), StudentViewModel.class);
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

    @Override
    @Transactional
    public void uploadPicture(MultipartFile file, Long studentId) throws IOException {


            Map<?, ?> cloudinaryResponse = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "public_id", "Student" + studentId + "profilePic.jpg"
                    ,"resource_type", "auto"));

            Optional<BaseUser> optionalBaseUser = userRepository.findById(studentId);
            if (optionalBaseUser.isEmpty()) {
                throw  new NotActiveException();
            }

            BaseUser user = optionalBaseUser.get();

            user.setProfilePicURL((String) cloudinaryResponse.get("url"));
            userRepository.saveAndFlush(user);


    }


}
