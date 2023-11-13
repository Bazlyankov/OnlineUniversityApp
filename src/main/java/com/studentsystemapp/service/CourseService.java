package com.studentsystemapp.service;
import com.studentsystemapp.model.binding.CourseAddBindingModel;
import com.studentsystemapp.model.binding.CourseResourceAddBindingModel;
import com.studentsystemapp.model.entity.Course;
import com.studentsystemapp.model.entity.CourseResource;
import com.studentsystemapp.model.view.CourseViewModel;

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

    void addResourceToCourse(Long id, CourseResourceAddBindingModel resourceAddBindingModel);

    Optional<Course> findById(Long courseId);

    Set<CourseViewModel> getCoursesByUsername(String name);
}
