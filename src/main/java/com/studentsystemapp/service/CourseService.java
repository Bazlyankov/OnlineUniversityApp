package com.studentsystemapp.service;
import com.studentsystemapp.model.binding.CourseAddBindingModel;
import com.studentsystemapp.model.binding.CourseResourceAddBindingModel;
import com.studentsystemapp.model.entity.Course;
import com.studentsystemapp.model.entity.CourseResource;
import com.studentsystemapp.model.view.CourseViewModel;
import com.studentsystemapp.model.view.EnrollmentViewModel;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CourseService{


public List<Course> getAll();

public CourseViewModel getCourseById(Long id);
public Optional<Course> getCourseByName(String name);

public boolean add(CourseAddBindingModel courseAddBindingModel);

    public void uploadTasksForCourse(Long courseId);

public boolean remove(Long courseId, Long studentId);


    void addStudent(Long id, String username);

    @Transactional
    void addStudent(String courseName, String username);

    void addResourceToCourse(Long id, CourseResourceAddBindingModel resourceAddBindingModel);

    @Transactional
    void addResourceToCourse(String courseName, CourseResourceAddBindingModel resourceAddBindingModel);

    Optional<Course> findById(Long courseId);

    Set<EnrollmentViewModel> getEnrollmentsByUsername(String name);

    void deleteAll();
}
