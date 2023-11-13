package com.studentsystemapp.service.impl;

import com.studentsystemapp.model.binding.CourseResourceAddBindingModel;
import com.studentsystemapp.model.entity.CourseResource;
import com.studentsystemapp.repo.CourseRepository;
import com.studentsystemapp.repo.ResourceRepository;
import com.studentsystemapp.service.ResourceService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.studentsystemapp.model.entity.Course;

import java.util.Optional;

@Service
public class ResourceServiceImpl implements ResourceService {

    private final CourseRepository courseRepository;
    private final ResourceRepository resourceRepository;
    private final ModelMapper modelMapper;

    public ResourceServiceImpl(CourseRepository courseRepository, ResourceRepository resourceRepository, ModelMapper modelMapper) {
        this.courseRepository = courseRepository;
        this.resourceRepository = resourceRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    @Transactional
    public boolean add(Long courseId, CourseResourceAddBindingModel courseResourceAddBindingModel) {

        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if (optionalCourse.isEmpty()) return false;

        CourseResource resource = modelMapper.map(courseResourceAddBindingModel, CourseResource.class);

        Course course = optionalCourse.get();
        course.getCourseResources().add(resource);

        resourceRepository.save(resource);
        courseRepository.saveAndFlush(course);

        return true;
    }

    @Override
    @Transactional
    public boolean delete(Long courseId, Long resourceId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        Optional<CourseResource> optionalResource = resourceRepository.findById(resourceId);

        if (optionalCourse.isEmpty() || optionalResource.isEmpty()) return false;

        Course course = optionalCourse.get();
        CourseResource resource = optionalResource.get();

        course.getCourseResources().remove(resource);
        resourceRepository.delete(resource);
        courseRepository.saveAndFlush(course);

        return true;
    }
}
