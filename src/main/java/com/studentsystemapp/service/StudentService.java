package com.studentsystemapp.service;

import com.studentsystemapp.model.binding.StudentRegisterBindingModel;
import com.studentsystemapp.model.entity.*;
import com.studentsystemapp.model.view.StudentViewModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StudentService {

    public int remove(String username);

    public boolean add(StudentRegisterBindingModel studentRegisterBindingModel);


    List<BaseUser> getAllStudents();

    public List<BaseUser> getAllByCourse(Course course);


    public StudentViewModel getById(Long id);

    void makeTeacher(Long id);

    void uploadPicture(MultipartFile file, Long studentId) throws IOException;
}
