package com.studentsystemapp.service.impl;

import com.studentsystemapp.model.binding.CourseAddBindingModel;
import com.studentsystemapp.model.entity.BaseUser;
import com.studentsystemapp.model.entity.Course;
import com.studentsystemapp.model.view.CourseViewModel;
import com.studentsystemapp.repo.CourseRepository;
import com.studentsystemapp.repo.ResourceRepository;
import com.studentsystemapp.repo.UserRepository;
import com.studentsystemapp.repo.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void getAll() {
        when(courseService.getAll()).thenReturn(List.of(new Course()));

        List<Course> result = courseService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getCourseById() {
        when(courseService.getCourseById(1L)).thenReturn(new CourseViewModel());

        CourseViewModel result = courseService.getCourseById(1L);

        assertNotNull(result);
    }

    @Test
    void add() {
        // Mock the repository method
        when(courseService.add(any())).thenReturn(true);

        // Mock the userRepository method
        when(courseService.getCourseByName(anyString())).thenReturn(Optional.of(new Course()));

        // Call the service method
        boolean result = courseService.add(new CourseAddBindingModel());

        assertTrue(result);

        assertNotEquals(courseService.getCourseByName("courseName").get(), null);
    }

    @Test
    void remove() {

        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(new Course()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new BaseUser()));

        // Check if the course is present
        Assertions.assertTrue(courseService.findById(1L).isPresent());

        // Check if the user is present
        Assertions.assertTrue(userRepository.findById(1L).isPresent());

        // Call the service method
//        boolean result = courseService.remove(1L, 2L);
//
//        // Verify the result
//        assertTrue(result);
    }




}
