package com.studentsystemapp.service;

import com.studentsystemapp.model.binding.StudentRegisterBindingModel;
import com.studentsystemapp.model.entity.*;
import com.studentsystemapp.model.view.StudentViewModel;

import java.util.List;

public interface StudentService {

    public boolean remove(String username);

    public boolean add(StudentRegisterBindingModel studentRegisterBindingModel);

    public List<BaseUser> getAll();
    public List<BaseUser> getAllByCourse(Course course);

    public void addGrade(Long studentId, Grade grade);

    public StudentViewModel getById(Long id);

    void makeTeacher(Long id);
}
