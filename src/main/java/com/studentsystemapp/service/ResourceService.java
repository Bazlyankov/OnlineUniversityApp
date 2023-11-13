package com.studentsystemapp.service;

import com.studentsystemapp.model.binding.CourseResourceAddBindingModel;

public interface ResourceService {

    public boolean add(Long courseId, CourseResourceAddBindingModel courseResourceAddBindingModel);
    public boolean delete(Long courseId, Long resourceId);

}
